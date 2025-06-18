package com.tfgmanuel.dungeonvault.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una nota dentro de la aplicación.
 *
 * @property id Identificador único de la nota (clave primaria)
 * @property campaign_id Identificador de la campaña en la que se encuentra.
 * @property user_id Identificador del uusuario al que pertenece la nota.
 * @property creation_date Fecha de creación de la nota.
 * @property title Título de la nota.
 * @property file_name Nombre del fichero de texto que contiene la info de la nota.
 * @property visibility Visibilidad de la nota.
 */ @Entity(
     tableName = "Note"
 )
data class Note(
    @PrimaryKey val id: Int,
    val campaign_id: Int,
    val user_id: Int?,
    val title: String,
    val creation_date: String,
    val file_name: String,
    val visibility: Boolean
)
