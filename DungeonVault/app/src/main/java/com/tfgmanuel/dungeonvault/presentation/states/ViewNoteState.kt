package com.tfgmanuel.dungeonvault.presentation.states

/**
 * Estado de la UI para visualizar y modificar una nota determinada
 *
 * @property title Titulo original de la nota.
 * @property newTitle Nuevo título de la nota.
 * @property readOnlyContent Indica si el contenido solo es de lectura (para trasncripciones).
 * @property content Contenido original de la nota.
 * @property newContent Nuevo contenido de la nota.
 * @property visibility Visibilidad original de la nota.
 * @property newVisibility Nueva visibilidad de la nota.
 * @property showDialog Booleano que indica si el diaólogo de decisión está o no activo.
 */
data class ViewNoteState(
    val title: String = "",
    val newTitle: String = "",
    val readOnlyContent: Boolean = false,
    val content: String = "",
    val newContent: String = "",
    val visibility: Boolean = false,
    val newVisibility: Boolean = false,
    val showDialog: Boolean = false
) {
    val isFormValid: Boolean
        get() = title.isNotBlank() && content.isNotBlank() && (title != newTitle || content != newContent || visibility != newVisibility)
}
