package com.tfgmanuel.dungeonvault.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa la información pública de un usuario dentro de la aplicacón.
 *
 * @param id Identificador del usuario (clave primaria).
 * @property email Correo electrónico del usuario.
 * @property avatar Nombre del fichero de imagen del avatar.
 * @property nickname Apodo del usuario dentro de la aplicación.
 * @property lastUpdated Timestamp de la última vez que se actualizó el elemento.
 */
@Entity(tableName = "Users")
data class User(
    @PrimaryKey val id: Int,
    val email: String,
    val avatar: String,
    val nickname: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
