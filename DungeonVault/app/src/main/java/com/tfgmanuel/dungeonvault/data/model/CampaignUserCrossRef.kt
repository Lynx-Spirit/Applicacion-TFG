package com.tfgmanuel.dungeonvault.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Representa la relación entre un usuario y una campaña.
 * Esta tabla intermedia permite asociar múltiples usuarios con múltiples campañas.
 *
 * @property campaignId El identificador de la campaña.
 * @property userId El identificador del usuario.
 *
 * Esta tabla no contiene más información, solo actúa como un "puente" entre las entidades Campaign y User.
 *
 * Relación: Muchos a muchos entre campañas y usuarios.
 */
@Entity(
    primaryKeys = ["campaignId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = Campaign::class,
            parentColumns = ["id"],
            childColumns = ["campaignId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CampaignUserCrossRef(
    val campaignId: Int,
    val userId: Int
)
