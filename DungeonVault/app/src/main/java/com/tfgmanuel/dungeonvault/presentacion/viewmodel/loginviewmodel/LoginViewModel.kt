package com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.presentacion.States.LoginState
import com.tfgmanuel.dungeonvault.presentacion.navigation.NavManager
import com.tfgmanuel.dungeonvault.presentacion.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationManager: NavManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onLoginChanged(email: String, password: String) {
        _uiState.value = _uiState.value.copy(email = email, password = password)
    }

    fun lonIn() {
        /**
         * Llamada a la API
         */
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