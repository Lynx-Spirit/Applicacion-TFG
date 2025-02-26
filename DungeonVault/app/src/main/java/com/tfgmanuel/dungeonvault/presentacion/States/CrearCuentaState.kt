package com.tfgmanuel.dungeonvault.presentacion.States

data class CrearCuentaState(
    val email: String = "",
    val password: String = "",
    val password2: String = "",
    val emailResult: String? = null,
    val passwordResult: String? = null,
    val confirmPasswordResult: String? = null
)
