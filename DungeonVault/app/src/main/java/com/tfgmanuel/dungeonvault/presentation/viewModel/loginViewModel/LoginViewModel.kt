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
            _uiState.value = _uiState.value.copy(error = null)
            if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
                _uiState.value = _uiState.value.copy(error = "Correo incorrecto")
            } else {
                val result = authRepository.login(_uiState.value.email, _uiState.value.password)
                if (result.isSuccess) {
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

    fun gotOCreateAccount() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CreateAccount.route)
        }
    }

    fun goToChangePassword() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.ChangePassword.route)
        }
    }
}