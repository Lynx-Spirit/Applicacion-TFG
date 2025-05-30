package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

/**
 * Estado de la UI utilizado para actualizar los datos del usuario.
 *
 * @property originalNickname Apodo original del usuario.
 * @property nickname Apodo actualizado del usuario.
 * @property originalAvatar Nombre original del fichero de imagen del avatar del usuario.
 * @property avatarUri URI del nuevo avatar del usuario.
 */
data class UpdateUserState(
    val originalNickname: String = "",
    val nickname: String = "",
    val originalAvatar: String = "",
    val avatarUri: Uri = Uri.EMPTY
) {
    /**
     * Indica si el formulario es válido.
     * Es válido si el apodo no es vacío y si el avatar no es vacío o el apodo no es igual al original.
     */
    val isFormValid: Boolean
        get() = nickname.isNotBlank() && (avatarUri != Uri.EMPTY || nickname != originalNickname)
}
