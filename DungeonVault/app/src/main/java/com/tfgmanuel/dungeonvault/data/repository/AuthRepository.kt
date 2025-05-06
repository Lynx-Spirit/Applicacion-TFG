package com.tfgmanuel.dungeonvault.data.repository

import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.model.RefreshTokenRequest
import com.tfgmanuel.dungeonvault.data.model.Token
import com.tfgmanuel.dungeonvault.data.model.User
import com.tfgmanuel.dungeonvault.data.remote.AuthAPI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authAPI: AuthAPI,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = authAPI.login(User(email, password))
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

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val response = authAPI.register(User(email, password))
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