package com.tfgmanuel.dungeonvault.presentation.states

import com.tfgmanuel.dungeonvault.data.model.Campaign

/**
 * Estado de la UI para la pantalla de detalles de la campaña.
 * Representa toda la inforamción necesaria para mostrar y controlar la vista.
 *
 * @property campaign Campaña actualmetne cargada para mostrar la información. Puede ser nulo
 * en caso de no haberse cargado.
 * @property hasPermission Indica si el usuario tiene los permisos para actualizar la campaña, será
 * true en caso de ser el creador.
 * @property showDialog Indica si se debe mostrar un cuadro de diálogo.
 * @property error Mensaje de error, en caso de no haber ningún error será nulo.
 */
data class CampaignDetailsState(
    val campaign: Campaign? = null,
    val hasPermission: Boolean = false,
    val showDialog: Boolean = false,
    val error: String? = null
)
