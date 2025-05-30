package com.tfgmanuel.dungeonvault.data.model

/**
 * Representa la respuesta de la API, tras haber realizado una autenticación exitosa o la renovación de los tokens.
 *
 * @property access_token Token que se utilizará para autenticar las peticiones del usuario.
 * @property refresh_token Token que permite solicitar un nuevo token de acceso cuando este expire.
 * @property token_type Tipo de token proporcionado, generalmente "Bearer".
 * @property user_id El identificador único del usuario autenticado.
 */
data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val user_id: Int
)