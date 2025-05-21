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

interface AuthAPI {

    @POST("auth/login")
    suspend fun login(@Body user: UserLogin): Response<TokenResponse>

    @POST("auth/register")
    suspend fun register(@Body user: UserRegister): Response<APIResponse>

    @GET("auth/get")
    suspend fun getUser(@Header ("Authorization") token: String): Response<User>

    @PUT("auth/update")
    suspend fun updateUser(
        @Body updateInfo: UserUpdateInfo,
        @Header ("Authorization") token: String
    ): Response<User>

    @POST("auth/refresh")
    suspend fun refresh(@Body tokenRequest: RefreshTokenRequest): Response<TokenResponse>

    @POST("auth/verify")
    suspend fun verify(@Body token: Token): Response<Boolean>

    @DELETE("auth/delete")
    suspend fun delete(@Header("Authorization") token: String): Response<APIResponse>
}