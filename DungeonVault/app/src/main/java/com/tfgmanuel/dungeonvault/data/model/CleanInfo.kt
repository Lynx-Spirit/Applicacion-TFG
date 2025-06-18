package com.tfgmanuel.dungeonvault.data.model

/**
 * Modelo de entrada que contiene toda la información necesaria para poder realizar la limpieza y creación del resumen.
 *
 * @property campaign_id Identificación de la campaña la cual se va a guardar los datos.
 * @property filename Nombre del fichero donde se va a guardar el resultado de la transcripción.
 */
data class CleanInfo(
    val campaign_id: Int,
    val filename: String
)
