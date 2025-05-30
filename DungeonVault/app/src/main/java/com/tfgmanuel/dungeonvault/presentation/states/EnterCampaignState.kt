package com.tfgmanuel.dungeonvault.presentation.states

/**
 * Estado de la UI utilizado para el proceso de acceso a la campaña.
 *
 * @property inviteCode Código de invitación para acceder a una campaña.
 * @property error Mensaje de error asociado al formulario, si existe.
 */
data class EnterCampaignState(
    val inviteCode: String = "",
    val error: String? = null
) {
    /**
     * Indica si la longitud del código de invitación son 6 caracteres.
     */
    val isInviteValid: Boolean get() = inviteCode.length == 6
}
