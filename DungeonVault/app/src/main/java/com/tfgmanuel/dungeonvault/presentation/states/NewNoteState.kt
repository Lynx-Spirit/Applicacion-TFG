package com.tfgmanuel.dungeonvault.presentation.states

/**
 * Estado de la UI para controlar la creación de una nueva nota.
 *
 * @property title Título de la nueva nota.
 * @property content Contenido de la nueva nota
 * @property visibility Visibilidad de la nueva nota.
 */
data class NewNoteState(
    val title: String = "",
    val content: String = "",
    val visibility: Boolean = false
) {
    val isFormValid: Boolean
        get() = title.isNotBlank() && content.isNotBlank()
}
