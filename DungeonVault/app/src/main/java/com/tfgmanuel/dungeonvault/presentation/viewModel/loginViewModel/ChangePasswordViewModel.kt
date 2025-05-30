package com.tfgmanuel.dungeonvault.presentation.viewModel.loginViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.presentation.states.ChangePasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de gestionar el estado y las acciones relacionadas con el cambio de contraseña.
 *
 * Este ViewModel mantiene el estado del formulario de recuperación de contraseña,
 * permitiendo actualizar el correo electrónico introducido por el usuario, gestionar
 * la navegación y (próximamente) lanzar el proceso de recuperación.
 *
 * @property navigationManager Gestor de navegación para controlar los flujos entre pantallas.
 */
@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val navigationManager: NavManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordState())
    val uiState: StateFlow<ChangePasswordState> = _uiState.asStateFlow()

    /**
     * Actualiza el correo electrónico en el estado del formulario de cambio de contraseña.
     *
     * @param email El nuevo valor del campo de correo electrónico ingresado por el usuario.
     */
    fun onChangePassChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    /**
     * Función encargada de iniciar el proceso de recuperación o cambio de contraseña.
     */
    fun changePassword() {
        TODO()
    }

    /**
     * Acción para regresar a la pantalla anterior.
     */
    fun goBack() {
        viewModelScope.launch {
            navigationManager.goBack()
        }
    }
}