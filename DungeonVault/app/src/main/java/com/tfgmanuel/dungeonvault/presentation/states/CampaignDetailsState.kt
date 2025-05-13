package com.tfgmanuel.dungeonvault.presentation.states

import com.tfgmanuel.dungeonvault.data.model.Campaign

data class CampaignDetailsState(
    val campaign: Campaign? = null,
    val hasPermission: Boolean = false,
    val showDialog: Boolean = false,
    val error: String? = null
)
