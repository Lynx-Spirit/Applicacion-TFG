package com.tfgmanuel.dungeonvault.presentation.states

data class ChangePasswordState(
    val email: String = "",
    val error: String? = null
)
