package com.tfgmanuel.dungeonvault.data.model

/**
 * Modelo de entrada con toda la información necesaria para poder realizar la transcripción.
 *
 * @property campaign_id Identificación de la campaña.
 * @property audio Nombre del fichero de audio a transcribir.
 * @property filename Fichero de texto donde se va a almacenar la infomración.
 */
data class TranscribeInfo(
    val campaign_id: Int,
    val audio: String,
    val filename: String
)
