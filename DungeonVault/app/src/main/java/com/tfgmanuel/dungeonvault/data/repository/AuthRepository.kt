package com.tfgmanuel.dungeonvault.data.repository

import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.model.RefreshTokenRequest
import com.tfgmanuel.dungeonvault.data.model.TokenResponse
import com.tfgmanuel.dungeonvault.data.model.User
import com.tfgmanuel.dungeonvault.data.remote.AuthAPI
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
                tokenManager.saveTokens(
                    authResponse.access_token,
                    authResponse.refresh_Token,
                    authResponse.token_type
                )
                Result.success("Login realizado con exito")
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val response = authAPI.register(User(email, password))
            if (response.isSuccessful) {
                Result.success("Registration successful")
            } else {
                Result.failure(Exception("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refresh(refreshToken: String): Result<TokenResponse> {
        return try {
            val response = authAPI.refresh(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                tokenManager.saveTokens(authResponse.access_token, authResponse.refresh_Token, authResponse.token_type)
                Result.success(authResponse) // Operación exitosa
            } else {
                Result.failure(Exception("Refresh failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}