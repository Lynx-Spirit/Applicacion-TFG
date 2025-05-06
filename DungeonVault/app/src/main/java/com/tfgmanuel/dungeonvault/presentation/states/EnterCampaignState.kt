package com.tfgmanuel.dungeonvault.presentation.states

data class EnterCampaignState(
    val inviteCode: String = "",
    val error: String? = null
) {
    val isInviteValid: Boolean get() = inviteCode.length == 6
}
