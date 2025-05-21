package com.tfgmanuel.dungeonvault.data.repository

import android.content.Context
import android.net.Uri
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.UserDataStore
import com.tfgmanuel.dungeonvault.data.model.RefreshTokenRequest
import com.tfgmanuel.dungeonvault.data.model.Token
import com.tfgmanuel.dungeonvault.data.model.User
import com.tfgmanuel.dungeonvault.data.model.UserLogin
import com.tfgmanuel.dungeonvault.data.model.UserRegister
import com.tfgmanuel.dungeonvault.data.model.UserUpdateInfo
import com.tfgmanuel.dungeonvault.data.remote.AuthAPI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authAPI: AuthAPI,
    private val imgRepository: ImgRepository,
    private val tokenManager: TokenManager,
    private val userDataStore: UserDataStore
) {

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = authAPI.login(UserLogin(email, password))
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                tokenManager.saveAll(
                    authResponse.access_token,
                    authResponse.refresh_token,
                    authResponse.token_type,
                    authResponse.user_id
                )
                Result.success("Login realizado con éxito")
            } else {
                val detail = getErrorFromApi(response)
                Result.failure(Exception(detail))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        nickname: String,
        avatarUri: Uri,
        context: Context
    ): Result<String> {
        return try {
            var avatar = ""

            if (avatarUri != Uri.EMPTY) {
                avatar = imgRepository.uploadImage(avatarUri, context)
            }

            val user = UserRegister(
                email = email,
                password = password,
                nickname = nickname,
                avatar = avatar
            )

            val response = authAPI.register(user)
            if (response.isSuccessful) {
                Result.success("Registro realizado con éxito")
            } else {
                val detail = getErrorFromApi(response)
                Result.failure(Exception(detail))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(): Result<String> {
        return firstAttemptGet()
            ?: refreshTokens()
                .flatMap { secondAttemptGet() }
    }

    private suspend fun firstAttemptGet(): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = authAPI.getUser("Bearer $access")

        if(response.isSuccessful) {
            return saveUser(response.body()!!, "Usuario obtenido correctamente")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun secondAttemptGet(): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = authAPI.getUser("Bearer $access")

        if(response.isSuccessful) {
            return saveUser(response.body()!!, "Usuario obtenido correctamente")
        }

        return Result.failure(Exception("No se ha podido obtener el usuario"))
    }

    suspend fun update(
        nickname: String = "",
        avatarUri: Uri = Uri.EMPTY,
        context: Context
    ): Result<String> {
        var avatar = ""

        if (avatarUri != Uri.EMPTY) {
            avatar = imgRepository.uploadImage(avatarUri, context)
        }

        val updateInfo = UserUpdateInfo(nickname = nickname, avatar = avatar)

        return firstAttemptUpdate(updateInfo)
            ?: refreshTokens()
                .flatMap { secondAttemptUpdate(updateInfo) }
    }

    private suspend fun firstAttemptUpdate(updateInfo: UserUpdateInfo): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = authAPI.updateUser(updateInfo = updateInfo, token = "Bearer $access")

        if (response.isSuccessful) {
           return saveUser(response.body()!!, "Usuario actualizado de forma correcta")
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun secondAttemptUpdate(updateInfo: UserUpdateInfo): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = authAPI.updateUser(updateInfo = updateInfo, token = "Bearer $access")

        if(response.isSuccessful) {
            return saveUser(response.body()!!, "Usuario actualizado de forma correcta")
        }

        return Result.failure(Exception("No se ha podido actualizar el usuario"))
    }

    private suspend fun saveUser(user: User, message: String): Result<String> {
        userDataStore.saveInformation(
            email = user.email,
            avatar = user.avatar,
            nickname = user.nickname
        )

        return Result.success(message)
    }

    suspend fun refreshTokens(): Result<String> {
        return try {
            val refreshToken = tokenManager.getRefreshToken().first()
                ?: return Result.failure(Exception("No se puede refrescar el token"))

            val response = authAPI.refresh(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                tokenManager.saveTokens(
                    authResponse.access_token,
                    authResponse.refresh_token,
                    authResponse.token_type
                )
                Result.success("Tokens refresacos de forma correcta")
            } else {
                val detail = getErrorFromApi(response)
                Result.failure(Exception(detail))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun userLoggedIn(): Boolean {
        val accessToken = tokenManager.getAccessToken().firstOrNull()
        val refreshToken = tokenManager.getRefreshToken().firstOrNull()

        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            return false
        }

        if (checkToken(accessToken)) {
            return true
        }

        return if (checkToken(refreshToken)) {
            val result = refreshTokens()
            result.isSuccess
        } else {
            false
        }
    }

    suspend fun deleteUser(): Result<String> {
        return firstAttemptDelete()
            ?: refreshTokens()
                .flatMap { secondAttemptDelete() }
    }

    private suspend fun firstAttemptDelete(): Result<String>? {
        val access = tokenManager.getAccessToken().first()
        val response = authAPI.delete("Bearer $access")

        if (response.isSuccessful) {
            return Result.success(response.body()!!.message)
        }

        if (response.code() != 401) {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }

        return null
    }

    private suspend fun secondAttemptDelete(): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = authAPI.delete("Bearer $access")

        if (response.isSuccessful) {
            return Result.success("Usuario eliminado de forma exitosa")
        } else {
            val detail = getErrorFromApi(response)
            return Result.failure(Exception(detail))
        }
    }

    suspend fun checkToken(token: String): Boolean {
        val response = authAPI.verify(Token(token))
        return response.isSuccessful
    }
}