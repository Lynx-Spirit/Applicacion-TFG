package com.tfgmanuel.dungeonvault.presentation.viewModel.loginViewModel

import android.content.Context
import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CreateAccountState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable del proceso de creación de cuenta en la aplicación.
 *
 * Maneja la validación de los datos introducidos por el usuario, el estado de la interfaz de usuario,
 * y la comunicación con el repositorio de autenticación para registrar nuevos usuarios.
 *
 * @property navigationManager Se encarga de manejar la navegación entre pantallas.
 * @property authRepository Repositorio encargado de la lógica de autenticación y registro.
 * @property contextProvider Proveedor de contexto, útil para acceder a recursos de Android durante el registro.
 */
@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val navigationManager: NavManager,
    private val authRepository: AuthRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {
    // Expresión regular para validar contraseñas: al menos 9 caracteres, una mayúscula y un número.
    private var passwordPattern = Regex("\\b(?=\\w{9,})(?=\\w*[A-Z])(?=\\w*\\d)\\w+\\b")

    private val _uiState = MutableStateFlow(CreateAccountState())
    val uiState: StateFlow<CreateAccountState> = _uiState.asStateFlow()

    /**
     * Actualiza el estado del formulario de registro con los nuevos valores proporcionados.
     *
     * @param email Dirección de correo electrónico introducida.
     * @param password Contraseña introducida.
     * @param confirmPassword Confirmación de la contraseña.
     * @param avatarUri URI del avatar seleccionado.
     * @param nickname Apodo elegido por el usuario.
     */
    fun onLoginChanged(
        email: String,
        password: String,
        confirmPassword: String,
        avatarUri: Uri,
        nickname: String
    ) {
        _uiState.value = _uiState.value
            .copy(
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                avatarUri = avatarUri,
                nickname = nickname
            )
    }

    /**
     * Valida los campos introducidos en el formulario de registro.
     * Si la validación es exitosa, inicia el proceso de registro llamando a [register].
     */
    fun registerUser() {

        _uiState.value = _uiState.value.copy(
            emailResult = null,
            passwordResult = null,
            confirmPasswordResult = null
        )

        if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            _uiState.value = _uiState.value.copy(emailResult = "Formato incorrecto de mail")
            return
        }

        if (!passwordPattern.matches(_uiState.value.password)) {
            _uiState.value = _uiState.value.copy(
                passwordResult =
                    "Contraseña debe tener más de 8 caracteres, una mayúscula y un número"
            )
            return
        }

        if (_uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.value =
                _uiState.value.copy(confirmPasswordResult = "Las contraseñas no coinciden")
            return
        }

        register(
            email = _uiState.value.email,
            password = _uiState.value.password,
            avatarUri = _uiState.value.avatarUri,
            nickname = _uiState.value.nickname,
            context = contextProvider.getContext()
        )

    }

    /**
     * Realiza el registro del usuario con los datos proporcionados.
     * En caso de éxito, redirige a la pantalla de inicio de sesión.
     * En caso de fallo, muestra el mensaje de error correspondiente.
     *
     * @param email Correo electrónico del nuevo usuario.
     * @param password Contraseña del nuevo usuario.
     * @param avatarUri URI del avatar del usuario.
     * @param nickname Apodo del usuario.
     * @param context Contexto de la aplicación (necesario para subir imagen, por ejemplo).
     */
    private fun register(
        email: String,
        password: String,
        avatarUri: Uri,
        nickname: String,
        context: Context
    ) {
        viewModelScope.launch {
            val result = authRepository.register(
                email = email,
                password = password,
                avatarUri = avatarUri,
                nickname = nickname,
                context = context
            )
            if (result.isSuccess) {
                navigationManager.navigate(
                    route = Screen.Login.route,
                    popUpTo = Screen.Login.route,
                    inclusive = true
                )
            } else {
                _uiState.value =
                    _uiState.value.copy(emailResult = result.exceptionOrNull()?.message)
            }
        }
    }

    /**
     * Acción para volver a la pantalla anterior.
     */
    fun goBack() {
        viewModelScope.launch {
            navigationManager.goBack()
        }
    }
}