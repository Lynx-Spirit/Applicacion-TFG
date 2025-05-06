package com.tfgmanuel.dungeonvault.presentation.states

import com.tfgmanuel.dungeonvault.data.model.Campaign

data class SelectCampaignState(
    val campaigns: List<Campaign> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
