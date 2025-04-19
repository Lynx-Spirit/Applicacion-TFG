package com.tfgmanuel.dungeonvault.presentacion.states

data class LoginState(
    val email: String = "",
    val password: String = "",
    val loginError: String? = null
)
