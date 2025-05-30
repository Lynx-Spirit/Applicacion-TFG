package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

/**
 * Estdo de la UI utilizado para actualizar una campaña.
 *
 * @property title Título actualizado de la camapaña.
 * @property originalTitle Título original de la campaña.
 * @property description Descripción actualizada de la campaña.
 * @property originalTitle Descripción original de la campaña.
 * @property imgName Nombre del fichero de imagen original de la campaña.
 * @property imgUri Nueva imagen de la campaña.
 * @property error Mensaje de error asociado al formulario, si existe.
 */
data class UpdateCampaignState(
    val title: String = "",
    val originalTitle: String = "",
    val description: String = "",
    val originalDescription: String = "",
    val imgName: String = "",
    val imgUri: Uri = Uri.EMPTY,
    val error: String? = null
) {
    /**
     * Indica si el formulario es válido.
     * Es válido siempre y cuando el título y descripción sean distintos al original y no son vacíos.
     */
    val isFormValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank()
                && (title != originalTitle || description != originalDescription || imgUri != Uri.EMPTY)
}
