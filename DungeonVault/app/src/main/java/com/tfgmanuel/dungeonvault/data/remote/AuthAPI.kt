package com.tfgmanuel.dungeonvault.data.remote

import com.tfgmanuel.dungeonvault.data.model.RefreshTokenRequest
import com.tfgmanuel.dungeonvault.data.model.RegisterResponse
import com.tfgmanuel.dungeonvault.data.model.Token
import com.tfgmanuel.dungeonvault.data.model.TokenResponse
import com.tfgmanuel.dungeonvault.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {

    @POST("auth/login")
    suspend fun login(@Body user: User): Response<TokenResponse>

    @POST("auth/register")
    suspend fun register(@Body user: User): Response<RegisterResponse>

    @POST("auth/refresh")
    suspend fun refresh(@Body tokenRequest: RefreshTokenRequest): Response<TokenResponse>

    @POST("auth/verify")
    suspend fun verify(@Body token: Token): Response<Boolean>
}