package com.tfgmanuel.dungeonvault.presentation.states

import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.model.User

/**
 * Estado de la UI para la pantalla de detalles de la campaña.
 * Representa toda la inforamción necesaria para mostrar y controlar la vista.
 *
 * @property campaign Campaña actualmetne cargada para mostrar la información. Puede ser nulo
 * en caso de no haberse cargado.
 * @property isLoading Indica si se está recargando o no la información.
 * @property showDialog Indica si se debe mostrar un cuadro de diálogo.
 * @property hasPermission Indica si el usuario tiene o no permisos para poder editar la campaña
 * @property error Mensaje de error, en caso de no haber ningún error será nulo.
 */
data class CampaignMainState(
    val campaign: Campaign? = null,
    val isLoading: Boolean = false,
    val showDialog: Boolean = false,
    val hasPermission: Boolean = false,
    val error: String? = null
)
