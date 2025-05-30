package com.tfgmanuel.dungeonvault.data.model

/**
 * Representa la información pública de un usuario dentro de la aplicacón.
 *
 * @property email Correo electrónico del usuario.
 * @property avatar Nombre del fichero de imagen del avatar.
 * @property nickname Apodo del usuario dentro de la aplicación.
 */
data class User(
    val email: String,
    val avatar: String,
    val nickname: String
)
