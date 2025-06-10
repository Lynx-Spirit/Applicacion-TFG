package com.tfgmanuel.dungeonvault.presentation.viewModel.loginViewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de manejar la lógica de autenticación (inicio de sesión) de la aplicación.
 *
 * Se encarga de actualizar el estado del formulario, validar los campos introducidos por el usuario,
 * comunicarse con el repositorio de autenticación y gestionar la navegación en función del resultado del login.
 *
 * @property navigationManager Manejador de navegación entre pantallas.
 * @property authRepository Repositorio de autenticación encargado de la lógica de login.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationManager: NavManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    /**
     * Actualiza el estado con los datos introducidos por el usuario.
     *
     * @param email Correo electrónico introducido.
     * @param password Contraseña introducida.
     */
    fun onLoginChanged(email: String, password: String) {
        _uiState.value = _uiState.value.copy(email = email, password = password)
    }

    /**
     * Ejecuta el proceso de autenticación.
     *
     * - Valida que el correo tenga un formato correcto.
     * - Si es válido, intenta iniciar sesión con el repositorio.
     * - Si el inicio es exitoso, navega a la pantalla principal.
     * - Si hay error, lo refleja en el estado para mostrarlo al usuario.
     */
    fun logIn() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null)
            if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
                _uiState.value = _uiState.value.copy(error = "Correo incorrecto")
            } else {
                val result = authRepository.login(_uiState.value.email, _uiState.value.password)
                if (result.isSuccess) {
                    authRepository.getUser()
                    navigationManager.navigate(
                        route = Screen.SelectCampaign.route,
                        popUpTo = Screen.SelectCampaign.route,
                        inclusive = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    /**
     * Navega a la pantalla de registro de cuenta nueva.
     */
    fun gotOCreateAccount() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CreateAccount.route)
        }
    }

    /**
     * Navega a la pantalla de recuperación o cambio de contraseña.
     */
    fun goToChangePassword() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.ChangePassword.route)
        }
    }
}