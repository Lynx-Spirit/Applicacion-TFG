package com.tfgmanuel.dungeonvault.data.repository

import androidx.lifecycle.ViewModel
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.model.RefreshTokenRequest
import com.tfgmanuel.dungeonvault.data.model.Token
import com.tfgmanuel.dungeonvault.data.model.TokenResponse
import com.tfgmanuel.dungeonvault.data.model.User
import com.tfgmanuel.dungeonvault.data.remote.AuthAPI
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authAPI: AuthAPI,
    private val tokenManager: TokenManager
) : ViewModel() {

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = authAPI.login(User(email, password))
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                tokenManager.saveTokens(
                    authResponse.access_token,
                    authResponse.refresh_token,
                    authResponse.token_type
                )
                Result.success("Login realizado con éxito")
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
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
                tokenManager.saveTokens(
                    authResponse.access_token,
                    authResponse.refresh_token,
                    authResponse.token_type
                )
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Refresh failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun userLoggedIn(): Boolean {
        var resultado: Boolean
        val accessToken = tokenManager.getAccessToken().firstOrNull()
        val refreshToken = tokenManager.getRefreshToken().firstOrNull()
        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            resultado = false
        } else {
            if (checkToken(accessToken)) {
                resultado = true
            } else if (checkToken(refreshToken)) {
                val result = refresh(refreshToken)
                resultado = result.isSuccess
            } else {
                resultado = false
            }
        }
        return resultado
    }

     suspend fun checkToken(token: String): Boolean {
        val response = authAPI.verify(Token(token))
        return response.isSuccessful
    }
}