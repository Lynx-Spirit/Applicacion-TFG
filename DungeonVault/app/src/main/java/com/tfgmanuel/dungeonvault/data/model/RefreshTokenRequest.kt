package com.tfgmanuel.dungeonvault.data.model

/**
 * Modelo utilizado para enviar una solicitud para la renovación de los token JWT.
 *
 * @property refresh_token Token de refresco proporcionado previamente durante el inicio de sesión o  durante algún refresco.
 */
data class RefreshTokenRequest(
    val refresh_token: String
)