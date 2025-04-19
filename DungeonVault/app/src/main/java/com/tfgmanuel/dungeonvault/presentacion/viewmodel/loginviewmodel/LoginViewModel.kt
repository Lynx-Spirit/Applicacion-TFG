package com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentacion.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationManager: NavManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onLoginChanged(email: String, password: String) {
        _uiState.value = _uiState.value.copy(email = email, password = password)
    }

    fun logIn() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loginError = null)
            val result = authRepository.login(_uiState.value.email,_uiState.value.password)
            if(result.isSuccess) {
                navigationManager.navigate(Screen.SeleccionCampania.route)
            }else {
                _uiState.value = _uiState.value.copy(loginError = "Usuario o contraseña incorrecta")
            }
        }
    }

    fun goToCrearCuenta() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CrearCuenta.route)
        }
    }

    fun goToCambiarPass() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CambiarContrasenia.route)
        }
    }
}