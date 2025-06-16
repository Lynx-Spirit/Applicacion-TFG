package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

/**
 * Estado de la UI encargado de controlar la parte de la creación de los personajes.
 *
 * @property name Nombre del personaje.
 * @property description Descripción del personaje.
 * @property backstory Texto que contiene el backstory.
 * @property imgUri Imagen asociada al personaje.
 * @property visibility Visibilidad del personaje.
 */
data class CreateCharacterState(
    val name: String = "",
    val description: String = "",
    val backstory: String = "",
    val imgUri: Uri = Uri.EMPTY,
    val visibility: Boolean = false
) {
    val isFormValid: Boolean
        get() = name.isNotBlank() && description.isNotBlank()
}
