package com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.presentacion.States.CrearCuentaState
import com.tfgmanuel.dungeonvault.presentacion.navigation.NavManager
import com.tfgmanuel.dungeonvault.presentacion.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearCuentaViewModel @Inject constructor(
    private  val navigationManager: NavManager
): ViewModel() {
    private var passwordPattern = Regex("\\b(?=\\w{9,})(?=\\w*[A-Z])(?=\\w*\\d)\\w+\\b")

    private val _uiState = MutableStateFlow(CrearCuentaState())
    val uiState: StateFlow<CrearCuentaState> = _uiState.asStateFlow()

    fun onLoginChanged(email: String, password: String, password2: String) {
        _uiState.value = _uiState.value
            .copy(email = email, password = password, password2 = password2)
    }

    fun registerUser() {

        _uiState.value = _uiState.value.copy(emailResult = null, passwordResult = null, confirmPasswordResult = null)

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

    fun goBack() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Inicio.route)
        }
    }
}