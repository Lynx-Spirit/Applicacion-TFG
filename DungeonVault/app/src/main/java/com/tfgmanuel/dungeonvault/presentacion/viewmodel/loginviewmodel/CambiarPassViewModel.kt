package com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.presentacion.states.CambiarPassState
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CambiarPassViewModel @Inject constructor(
    private val navigationManager: NavManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CambiarPassState())
    val uiState: StateFlow<CambiarPassState> = _uiState.asStateFlow()

    fun onChangePassChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun changePass() {

    }

    fun goBack() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Inicio.route)
        }
    }
}