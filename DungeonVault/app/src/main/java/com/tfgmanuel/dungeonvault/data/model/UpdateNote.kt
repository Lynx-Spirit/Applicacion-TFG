package com.tfgmanuel.dungeonvault.data.model

/**
 * Representa la información de una nota para actualizarla.
 *
 * @property title Título de la nota.
 * @property file_name Nombre del fichero de texto que contiene la info de la nota.
 * @property visibility Visibilidad de la nota.
 */
data class UpdateNote(
    val title : String,
    val file_name: String,
    val visibility: Boolean
)
