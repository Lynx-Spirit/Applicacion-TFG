package com.tfgmanuel.dungeonvault.presentation.states

/**
 * Estado de la UI utilizado para el proceso de cambio de contraseña.
 *
 * @property email Correo electrónico del usuario que solicita el cambio de contraseña.
 * @property error Mensaje de error, si ocurrió alguno durante la operación.
 */
data class ChangePasswordState(
    val email: String = "",
    val error: String? = null
)
