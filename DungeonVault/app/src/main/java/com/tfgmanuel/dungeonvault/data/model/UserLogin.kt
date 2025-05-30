package com.tfgmanuel.dungeonvault.data.model

/**
 * Representa las credenciales de un usuario para hacer el login dentro de la aplicación.
 *
 * @property email Correo electrónico del usuario.
 * @property password Contraseña del usuario.
 */
data class UserLogin(
    val email: String,
    val password: String
)