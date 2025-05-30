package com.tfgmanuel.dungeonvault.data.model

/**
 * Representa el modleo para la creación de una campaña por parte de un usuario.
 *
 * @property title Título de la campaña a crear.
 * @property description Descripción de la campaña a crear.
 * @property img_name Nombre del archivo de imagen asociada a la campaña a crear.
 */
data class CreateCampaign(
    val title: String,
    val description: String,
    val img_name: String
)
