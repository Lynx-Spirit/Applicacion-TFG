package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

data class CreateAccountState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nickname: String = "",
    val avatarUri: Uri = Uri.EMPTY,
    val emailResult: String? = null,
    val passwordResult: String? = null,
    val confirmPasswordResult: String? = null
)
