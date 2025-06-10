package com.tfgmanuel.dungeonvault.data.model

/**
 * Representa la informacíon necesaria para poder echar a un usuario determinado de una campaña.
 *
 * @property user Identificador del usuario que se quiere echar.
 * @property id Identificador de la campaña.
 */
data class KickInformation(
    val user: Int,
    val id: Int
)
