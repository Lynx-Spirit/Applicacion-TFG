package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.APIResponse
import com.tfgmanuel.dungeonvault.data.model.RefreshTokenRequest
import com.tfgmanuel.dungeonvault.data.model.Token
import com.tfgmanuel.dungeonvault.data.model.TokenResponse
import com.tfgmanuel.dungeonvault.data.model.User
import com.tfgmanuel.dungeonvault.data.model.UserLogin
import com.tfgmanuel.dungeonvault.data.model.UserRegister
import com.tfgmanuel.dungeonvault.data.model.UserUpdateInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 * Interfaz que define las llamadas a la API relacionadas con la autorización y gestión del usuario.
 * Utiliza Retrofit para mapear las peticiones HTTP.
 */
interface AuthAPI {

    /**
     * Inicia sesión de un usuario utilizando su correo y contraseña.
     *
     * @param user Credenciales del usuario.
     *
     * @return Un [TokenResponse] envuelto en un [Response] si el inicio de sesión es exitoso.
     */
    @POST("auth/login")
    suspend fun login(@Body user: UserLogin): Response<TokenResponse>

    /**
     * Registro de un nuevo usuario en el sistema.
     *
     * @param user Datos de registro de del nuevo usuario. Incluye: email, contraseña, avatar y apodo.
     *
     * @return Un [APIResponse] que contiene los tokens de refresco y acceso, el tipo de token y el ID del usuario, envuelto en un [Response]
     * si el registro es exitoso.
     */
    @POST("auth/register")
    suspend fun register(@Body user: UserRegister): Response<APIResponse>

    /**
     * Obtención de la información de un usuario autenticado.
     *
     * @param token Token de autorización (Bearer).
     *
     * @return Un [User] con la información del usuario envuelvo en un [Response] si la obtención de la informacíon es exitoso.
     */
    @GET("auth/get")
    suspend fun getUser(@Header ("Authorization") token: String): Response<User>

    /**
     * Actualiza la inforamción del usuario autenticado.
     *
     * @param updateInfo Nuevos datos del usuario.
     * @param token Token de autorización (Bearer).
     *
     * @return Un [User] con todos los datos actualizados envuelto en un [Response]
     */
    @PUT("auth/update")
    suspend fun updateUser(
        @Body updateInfo: UserUpdateInfo,
        @Header ("Authorization") token: String
    ): Response<User>

    /**
     * Refresco de los tokens mediante el token de refresco.
     *
     * @param tokenRequest Contiene el token de refresco.
     *
     * @return [Response] que envuelve un [TokenResponse] con los nuevos tokens de acceso y refresco,
     * si el refresco se realizó de forma correcta.
     */
    @POST("auth/refresh")
    suspend fun refresh(@Body tokenRequest: RefreshTokenRequest): Response<TokenResponse>

    /**
     * Verifica la validez de un token.
     *
     * @param token Token a verificar.
     * @return [Response] que contiene un [Boolean] indicando si el token es válido.
     */
    @POST("auth/verify")
    suspend fun verify(@Body token: Token): Response<Boolean>

    /**
     * Elimina la cuenta de un usuario autenticado.
     *
     * @param token Token de autorización (Bearer).
     * @return [Response] que contiene un [APIResponse] con el resultado de la operación.
     */
    @DELETE("auth/delete")
    suspend fun delete(@Header("Authorization") token: String): Response<APIResponse>
}