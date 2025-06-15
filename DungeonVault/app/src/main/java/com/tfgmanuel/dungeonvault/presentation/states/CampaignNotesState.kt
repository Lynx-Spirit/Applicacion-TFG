package com.tfgmanuel.dungeonvault.presentation.states

import com.tfgmanuel.dungeonvault.data.model.Note

/**
 * Estado de la UI para la pantalla de notas de la campaña.
 * Representa toda la informacíon necesaria para mostrar y controlar la vista.
 *
 * @property notes [List] de todas las notas que tiene la campaña.
 * @property isLoading Indica si se está recargando o no la información.
 * @property userID  Identificador del usuario que está mirando las notas.
 * @property creatorID Identificador del creador de la campaña.
 * @property error Mensaje de error, en caso de no haber ningún error será nulo.
 */
data class CampaignNotesState(
    val notes: List<Note> = emptyList(),
    val userID: Int = 0,
    val creatorID: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
