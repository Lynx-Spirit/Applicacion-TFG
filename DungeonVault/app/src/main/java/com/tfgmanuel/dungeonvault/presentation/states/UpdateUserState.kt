package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

data class UpdateUserState(
    val originalNickname: String = "",
    val nickname: String = "",
    val originalAvatar: String = "",
    val avatarUri: Uri = Uri.EMPTY
) {
    val isFormValid: Boolean
        get() = nickname.isNotBlank() && (avatarUri != Uri.EMPTY || nickname != originalNickname)
}
