package com.tfgmanuel.dungeonvault.presentation.states

data class CreateAccountState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailResult: String? = null,
    val passwordResult: String? = null,
    val confirmPasswordResult: String? = null
)
