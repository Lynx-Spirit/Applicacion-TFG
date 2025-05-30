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

    /**
     * Inicio de sesión del usuario con sus credenciales.
     *
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     *
     * @return [Result] con el resultado del login.
     *
     * @throws exception En caso de haber un error desconocido, se lanza un mensaje con el error concreto.
     */
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

    /**
     * Regristro de un nuevo usuario en la aplicación.
     *
     * @param email Correo electrónico del nuevo usuario.
     * @param password Contraseña del usuario.
     * @param nickname Apodo del nuevo usaurio.
     * @param avatarUri Nombre del fichero de imagen del avatar del nuevo usuario.
     * @param context Contexto de la aplicación.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     *
     * @throws exception En caso de haber un error desconocido, se lanza un mesnaje con el error conreto.
     */
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

    /**
     * Obtanción de la información del usuario.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    suspend fun getUser(): Result<String> {
        return firstAttemptGet()
            ?: refreshTokens()
                .flatMap { secondAttemptGet() }
    }

    /**
     * Primer intento de obtención de la información del usuario.
     * En caso de quedevuelva la API un 401, retorna null para poder refrescar los tokens.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Segundo y último intento de obtención de la información del usuario tras haber refrescado los tokens.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptGet(): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = authAPI.getUser("Bearer $access")

        if(response.isSuccessful) {
            return saveUser(response.body()!!, "Usuario obtenido correctamente")
        }

        return Result.failure(Exception("No se ha podido obtener el usuario"))
    }

    /**
     * Actualización de la información de un usuario autenticado.
     *
     * @param nickname Nuevo apodo del usuario.
     * @param avatarUri Nuevo fichero de imagen del avatar del usuario.
     * @param context Contexto de la aplicación.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Primer intento de actualización de la información del usuario.
     * En caso de quedevuelva la API un 401, retorna null para poder refrescar los tokens.
     *
     * @param updateInfo Objeto que engloba toda la información para actualizar los datos del usuario.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Segundo y último intento de actualizar la información del usuario tras haber refrescado los tokens.
     *
     * @param updateInfo Objeto que engloba toda la información para actualizar los datos del usuario.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun secondAttemptUpdate(updateInfo: UserUpdateInfo): Result<String> {
        val access = tokenManager.getAccessToken().first()
        val response = authAPI.updateUser(updateInfo = updateInfo, token = "Bearer $access")

        if(response.isSuccessful) {
            return saveUser(response.body()!!, "Usuario actualizado de forma correcta")
        }

        return Result.failure(Exception("No se ha podido actualizar el usuario"))
    }

    /**
     * Almacenaje de la informacíon del usuario en [userDataStore]
     *
     * @param user Objeto [User] que contiene la información para almacenar.
     * @param message Mensaje en caso de ser correcta la ejecución.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
    private suspend fun saveUser(user: User, message: String): Result<String> {
        userDataStore.saveInformation(
            email = user.email,
            avatar = user.avatar,
            nickname = user.nickname
        )

        return Result.success(message)
    }

    /**
     * Refresco y almacenaje de los tokens a partir del token de refresco.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     *
     * @throws exception En caso de haber un error desconocido, se lanza un mesnaje con el error conreto.
     */
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

    /**
     * Checkea si el usuario ha iniciado sesión en la aplicación o no.
     *
     * @return [Boolean] que devuelve True si tiene la sesión iniciada y False en caso contrario.
     */
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

    /**
     * Elimina los datos del usuario de la aplicación.
     *
     * @return Devuelve un [Result] con un [String] indicando el resultado de la ejecución.
     */
    suspend fun deleteUser(): Result<String> {
        return firstAttemptDelete()
            ?: refreshTokens()
                .flatMap { secondAttemptDelete() }
    }

    /**
     * Primer intento de eliminación de la información del usuario.
     * En caso de quedevuelva la API un 401, retorna null para poder refrescar los tokens.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Segundo y último intento de eliminación de la información del usuario tras haber refrescado los tokens.
     *
     * @return [Result] que contiene un [String] con el resultado de la ejecución.
     */
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

    /**
     * Checkeo de la validez del token pasado como parámetro.
     *
     * @param token Token a verificar.
     *
     * @return [Boolean] que devuelve True en caso de de ser válido y False en caso contrario
     */
    suspend fun checkToken(token: String): Boolean {
        val response = authAPI.verify(Token(token))
        return response.isSuccessful
    }
}