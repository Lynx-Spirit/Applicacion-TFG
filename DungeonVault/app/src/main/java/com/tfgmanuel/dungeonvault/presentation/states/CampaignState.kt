package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

/**
 * Estado de la UI utilizado para crear una nueva campaña.
 *
 * @property title Titulo de la nueva campaña. No puede estar vacío para que el formulario sea válido.
 * @property description Descripción de la nueva campaña. También debe ser no vacía.
 * @property imgUri URI de la imagen seleccionada para la campaña. Por defecto está vacío.
 * @property error Mensaje de error asociado al formulario, si existe.
 */
data class CampaignState(
    val title: String = "",
    val description: String = "",
    val imgUri: Uri = Uri.EMPTY,
    val error: String? = null
) {
    /**
     * Indica si el formulario es válido.
     * Es válido siempre y cuando el título y la descripción no están en blanco
     */
    val isFormValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank()
}
