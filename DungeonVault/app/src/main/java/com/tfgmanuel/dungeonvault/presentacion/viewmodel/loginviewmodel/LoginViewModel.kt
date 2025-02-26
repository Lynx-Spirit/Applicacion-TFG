package com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel

import androidx.lifecycle.ViewModel
import com.tfgmanuel.dungeonvault.presentacion.States.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {

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


}