package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

data class UpdateCampaignState(
    val title: String = "",
    val originalTitle: String = "",
    val description: String = "",
    val originalDescription: String = "",
    val imgName: String = "",
    val imgUri: Uri = Uri.EMPTY,
    val error: String? = null
) {
    val isFormValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank()
                && (title != originalTitle || description != originalDescription || imgUri != Uri.EMPTY)
}
