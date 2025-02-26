package com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.tfgmanuel.dungeonvault.presentacion.States.CrearCuentaState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CrearCuentaViewModel : ViewModel() {
    private var passwordPattern = Regex("\\b(?=\\w{9,})(?=\\w*[A-Z])(?=\\w*\\d)\\w+\\b")

    private val _uiState = MutableStateFlow(CrearCuentaState())
    val uiState: StateFlow<CrearCuentaState> = _uiState.asStateFlow()

    fun onLoginChanged(email: String, password: String, password2: String) {
        _uiState.value = _uiState.value
            .copy(email = email, password = password, password2 = password2)
    }

    fun registerUser() {

        if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            _uiState.value = _uiState.value.copy(emailResult = "Formato incorrecto de mail")
            return
        }

        if (passwordPattern.matches(_uiState.value.password)) {
            _uiState.value = _uiState.value.copy(
                passwordResult =
                "Contraseña debe tener más de 8 caracteres, una mayúscula y un número"
            )
            return
        }

        if (_uiState.value.password != _uiState.value.password2) {
            _uiState.value =
                _uiState.value.copy(confirmPasswordResult = "Las contraseñas no coinciden")
            return
        }

        /**
         * Llamada a la API
         **/
    }
}