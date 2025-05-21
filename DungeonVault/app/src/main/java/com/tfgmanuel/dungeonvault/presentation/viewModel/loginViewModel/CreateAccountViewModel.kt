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

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val navigationManager: NavManager,
    private val authRepository: AuthRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private var passwordPattern = Regex("\\b(?=\\w{9,})(?=\\w*[A-Z])(?=\\w*\\d)\\w+\\b")

    private val _uiState = MutableStateFlow(CreateAccountState())
    val uiState: StateFlow<CreateAccountState> = _uiState.asStateFlow()

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

    fun goBack() {
        viewModelScope.launch {
            navigationManager.goBack()
        }
    }
}