package com.tfgmanuel.dungeonvault.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una campaña dentro de la aplicación.
 *
 * @property id Identificador único de campaña (clave primaria).
 * @property title Título de la campaña.
 * @property description Descripción de la campaña.
 * @property img_name Nombre del archivo de imagen asociada a la campaña.
 * @property invite_code Código único de invitacíon de campaña. Permite a otros jugadores unirse a la campaña.
 * @property creator_id Identificador del usuario que creó la campaña.
 * @property lastUpdated Timestamp de la última vez que se actualizó el elemento.
 */
@Entity(
    tableName = "Campaigns"
)
data class Campaign(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val img_name: String,
    val invite_code: String,
    val creator_id: Int,
    var lastUpdated: Long = System.currentTimeMillis()
)
