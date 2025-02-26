package com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel

import androidx.lifecycle.ViewModel
import com.tfgmanuel.dungeonvault.presentacion.States.CambiarPassState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CambiarPassViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(CambiarPassState())
    val uiState: StateFlow<CambiarPassState> = _uiState.asStateFlow()

    fun onChangePassChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun changePass() {

    }
}