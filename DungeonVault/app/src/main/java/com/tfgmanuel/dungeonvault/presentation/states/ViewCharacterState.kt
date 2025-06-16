package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

/**
 * Estado de la UI encargado de controlar la parte de la visualización y modificación de los personajes.
 *
 * @property name Nombre del personaje.
 * @property newName Nuevo nombre del personaje.
 * @property description Descripción del personaje.
 * @property newDescription Nueva descipción del personaje.
 * @property backstory Texto que contiene el backstory.
 * @property newBackstory Nuevo texto que contiene el backstory.
 * @property originalImg Nombre del fichero de imagen original asociado al personaje.
 * @property imgUri Imagen asociada al personaje.
 * @property visibility Visibilidad del personaje.
 * @property newVisibility Nueva visibilidad del personaje.
 * @property readOnly Indica si el contenido se de solo lectura.
 * @property showDialog Indica si el diálogo de decisión está abierto o no.
 */
data class ViewCharacterState(
    val name: String = "",
    val newName: String = "",
    val description: String = "",
    val newDescription: String = "",
    val backstory: String = "",
    val newBackstory: String = "",
    val originalImg: String = "",
    val imgUri: Uri = Uri.EMPTY,
    val visibility: Boolean = false,
    val newVisibility: Boolean = false,
    val readOnly: Boolean = false,
    val showDialog: Boolean = false
) {
    val isFormValid: Boolean
        get() = name.isNotBlank() && description.isNotBlank() && (name != newName || description != newDescription || backstory != newBackstory || imgUri != Uri.EMPTY || visibility != newVisibility)
}