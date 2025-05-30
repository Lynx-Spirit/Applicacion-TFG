package com.tfgmanuel.dungeonvault.presentation.states

/**
 * Estado de la UI utilizado para el proceso de inicio de sesión.
 *
 * @property email Correo electrónico del usuario.
 * @property password Contraseña del usuario.
 * @property error Mensaje de error asociado al formulario, si existe.
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val error: String? = null
)
