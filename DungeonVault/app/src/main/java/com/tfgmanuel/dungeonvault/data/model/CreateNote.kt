package com.tfgmanuel.dungeonvault.data.model

/**
 * Representa la información de una nota para crearla.
 *
 * @property campaign_id Identificador de la campaña en la que se encuentra.
 * @property title Título de la nota.
 * @property file_name Nombre del fichero de texto que contiene la info de la nota.
 * @property visibility Visibilidad de la nota.
 */
data class CreateNote(
    val campaign_id: Int,
    val title : String,
    val file_name: String,
    val visibility: Boolean
)
