package com.tfgmanuel.dungeonvault.presentation.viewmodel.campaniaviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.EnterCampaignState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterCampaignViewModel @Inject constructor(
    private val navManager: NavManager,
    private val campaignRepository: CampaignRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EnterCampaignState())
    val uiState: StateFlow<EnterCampaignState> = _uiState.asStateFlow()

    fun onInviteChange(inviteCode: String) {
        _uiState.value = _uiState.value.copy(
            inviteCode = inviteCode.uppercase()
        )
    }

    fun onInviteSelected() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null)
            val result =
                campaignRepository.insertUser(uiState.value.inviteCode.uppercase())
            if (result.isSuccess) {
                goBack()
            } else {
                val errorMessage =
                    result.exceptionOrNull()?.message ?: "Error al unirse a la partida."
                _uiState.value = _uiState.value.copy(error = errorMessage)
            }
        }
    }

    fun goBack() {
        viewModelScope.launch {
            navManager.navigate(Screen.SelectCampaign.route)
        }
    }
}