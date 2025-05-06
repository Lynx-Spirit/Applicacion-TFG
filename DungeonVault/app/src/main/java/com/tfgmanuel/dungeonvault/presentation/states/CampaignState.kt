package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

data class CampaignState(
    val title: String = "",
    val description: String = "",
    val imgUri: Uri = Uri.EMPTY,
    val error: String? = null
) {
    val isFormValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank()
}
