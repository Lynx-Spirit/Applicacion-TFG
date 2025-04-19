package com.tfgmanuel.dungeonvault.presentacion.states

import com.tfgmanuel.dungeonvault.data.model.Campaign

data class SeleccionCamapaniaState(
    val campanias: List<Campaign> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
