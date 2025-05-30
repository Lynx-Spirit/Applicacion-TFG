package com.tfgmanuel.dungeonvault.presentation.states

import android.net.Uri

/**
 * Estado de la UI utilizado para crear una cuenta
 *
 * @property email Correo electrónico del nuevo usuario.
 * @property password Contraseña del nuevo usuario.
 * @property confirmPassword Contraseña confirmada del nuevo usuario, esta debe ser igual a [password]
 * @property nickname Apodo del nuevo usuario.
 * @property avatarUri URI de la imagen seleecionada para el usuario. Por defecto está vacío.
 * @property emailResult Mensaje de error asociado al email, si existe.
 * @property passwordResult Mensaje de error asociado a la contraseña, si existe.
 * @property confirmPasswordResult Mensaje de error asociado a [confirmPassword] en caso de que no
 * sea igual a [password].
 */
data class CreateAccountState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nickname: String = "",
    val avatarUri: Uri = Uri.EMPTY,
    val emailResult: String? = null,
    val passwordResult: String? = null,
    val confirmPasswordResult: String? = null
)
