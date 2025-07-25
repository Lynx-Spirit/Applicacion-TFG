package com.tfgmanuel.dungeonvault.presentation.states

import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.model.User

/**
 * Estado de la UI para la pantalla de detalles de la campaña.
 * Representa toda la inforamción necesaria para mostrar y controlar la vista.
 *
 * @property campaign Campaña actualmetne cargada para mostrar la información. Puede ser nulo
 * en caso de no haberse cargado.
 * @property members [List] que contiene todos los miembros de la campaña.
 * @property isLoading Indica si se está recargando o no la información.
 * @property showDeleteDialog Indica si se debe mostrar un cuadro de diálogo.
 * @property showKickDialog Indica si se debe mostrar un cuadro de diálogo.
 * @property showLeaveDialog Indica si se debe mostrar un cuadro de diálogo.
 * @property hasPermission Indica si el usuario tiene o no permisos para poder editar la campaña
 * @property error Mensaje de error, en caso de no haber ningún error será nulo.
 */
data class CampaignDetailsState(
    val campaign: Campaign? = null,
    val creatorId: Int = 0,
    val members: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val showLeaveDialog: Boolean = false,
    val showKickDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val hasPermission: Boolean = false,
    val error: String? = null
)