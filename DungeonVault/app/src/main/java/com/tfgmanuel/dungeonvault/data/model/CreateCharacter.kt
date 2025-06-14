package com.tfgmanuel.dungeonvault.data.model

import androidx.room.PrimaryKey

/**
 * Información necesaria para poder crear un personaje.
 *  @property campaign_id Identificador de la campaña en la que se encuentra.
 *  @property name Nombre del personaje.
 *  @property description Descripción del personaje.
 *  @property filename_backstory Nombre del fichero de texto que contiene el backstory.
 *  @property img_name Nombre del archivo de imagen asociado al personaje.
 *  @property visibility Visibilidad del personaje.
 */
data class CreateCharacter(
    val campaign_id: Int,
    val name: String,
    val description: String,
    val filename_backstory: String,
    val img_name: String,
    val visibility: Boolean
)
