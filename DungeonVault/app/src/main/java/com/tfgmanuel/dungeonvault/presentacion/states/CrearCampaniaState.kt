package com.tfgmanuel.dungeonvault.presentacion.states

import android.net.Uri

data class CrearCampaniaState(
    val titulo: String = "",
    val description: String = "",
    val imgUri: Uri = Uri.EMPTY,
    val mensajeError: String? = null
)
