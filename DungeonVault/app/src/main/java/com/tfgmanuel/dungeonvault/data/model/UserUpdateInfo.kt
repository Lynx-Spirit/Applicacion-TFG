package com.tfgmanuel.dungeonvault.data.model

/**
 * Rpresenta el modelo para la actualización de los datos públicos de un usuario.
 * En caso de no querer actualizarse, se manda vacío el campo.
 *
 * @property nickname Nuevo apodo del usuario.
 * @property avatar Nuevo archivo de imagen del usuario.
 */
data class UserUpdateInfo(
    val nickname: String,
    val avatar: String
)
