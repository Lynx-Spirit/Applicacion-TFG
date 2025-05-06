package com.tfgmanuel.dungeonvault.data.model

data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val user_id: Int
)