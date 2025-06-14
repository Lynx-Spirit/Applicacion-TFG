package com.tfgmanuel.dungeonvault.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa un personaje dentro de la aplicación.
 *
 * @property id Identificador único del personaje (clave primaria)
 * @property campaign_id Identificador de la campaña en la que se encuentra.
 * @property user_id Identificador del uusuario al que pertenece el personaje.
 * @property name Nombre del personaje.
 * @property description Descripción del personaje.
 * @property filename_backstory Nombre del fichero de texto que contiene el backstory.
 * @property img_name Nombre del archivo de imagen asociado al personaje.
 * @property visibility Visibilidad del personaje.
 */
@Entity(
    tableName = "Character"
)
data class Character(
    @PrimaryKey val id: Int,
    val campaign_id: Int,
    val user_id: Int,
    val name: String,
    val description: String,
    val filename_backstory: String,
    val img_name: String,
    val visibility: Boolean
)
