package com.tfgmanuel.dungeonvault.presentation.states

import com.tfgmanuel.dungeonvault.data.model.Character

/**
 * Estado de la UI para la pantalla de selección de personajes de la campaña.
 * Representa toda la información necesaria para mostrar y controlar la vista.
 *
 * @property characters [List] de todos los personajes que tiene la campaña.
 * @property userID Identificador del ususario que está mirando las notas.
 * @property creatorID Identificador del GM.
 * @property isLoading Indica si se está recrgando o no la información.
 */
data class CharacterSelectionState(
    val characters: List<Character> = emptyList(),
    val userID: Int = 0,
    val creatorID: Int = 0,
    val isLoading: Boolean = false
)
