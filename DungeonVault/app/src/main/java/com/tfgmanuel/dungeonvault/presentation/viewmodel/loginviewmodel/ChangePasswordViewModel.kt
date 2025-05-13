package com.tfgmanuel.dungeonvault.presentation.viewmodel.loginviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.presentation.states.ChangePasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val navigationManager: NavManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordState())
    val uiState: StateFlow<ChangePasswordState> = _uiState.asStateFlow()

    fun onChangePassChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun changePassword() {

    }

    fun goBack() {
        viewModelScope.launch {
            navigationManager.goBack()
        }
    }
}