package com.tfgmanuel.dungeonvault.presentacion.States

data class LoginState(
    val email: String = "",
    val password: String = "",
    val loginError: String? = null
)
