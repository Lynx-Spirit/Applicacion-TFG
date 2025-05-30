package com.tfgmanuel.dungeonvault.data.model

/**
 * Representa la información requerida para el registro de un usuario.
 *
 * @property email Correo electrónico del usuario.
 * @property password Contraseña del usuario.
 * @property avatar Archivo de imagen del avatar.
 * @property nickname Apodo del usuario.
 */
data class UserRegister(
    val email: String,
    val password: String,
    val avatar: String,
    val nickname: String
)
