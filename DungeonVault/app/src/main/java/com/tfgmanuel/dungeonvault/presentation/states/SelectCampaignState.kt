package com.tfgmanuel.dungeonvault.presentation.states

import com.tfgmanuel.dungeonvault.data.model.Campaign

/**
 * Estado de la UI utilizado para la selección de una campaña.
 *
 * @property campaigns [List] formado de objectos [Campaign], para mostrar la información de las campañas.
 * En caso de no haber campañas que mostrar, la lista estará vacía.
 * @property isLoading indica si se está cargando las campañas en la aplicación.
 * @property error Mensaje de error asociada a la pantalla, si existe.
 */
data class SelectCampaignState(
    val campaigns: List<Campaign> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
