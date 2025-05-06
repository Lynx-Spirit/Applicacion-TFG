package com.tfgmanuel.dungeonvault.presentation.states

data class LoginState(
    val email: String = "",
    val password: String = "",
    val error: String? = null
)
