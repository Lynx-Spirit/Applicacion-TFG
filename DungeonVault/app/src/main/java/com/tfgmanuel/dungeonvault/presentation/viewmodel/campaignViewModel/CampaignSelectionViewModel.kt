package com.tfgmanuel.dungeonvault.presentation.viewmodel.campaignViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.SelectCampaignState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignSelectionViewModel @Inject constructor(
    private val navigationManager: NavManager,
    private val campaignRepository: CampaignRepository,
    private val campaignDAO: CampaignDAO
): ViewModel() {
    private val _uiState = MutableStateFlow(SelectCampaignState())
    val uiState: StateFlow<SelectCampaignState> = _uiState.asStateFlow()

    init {
        loadCampaigns()
    }

    fun loadCampaigns(forceUpdate: Boolean = false) {
        _uiState.value = _uiState.value.copy(error = null)
        viewModelScope.launch {
            try {
                if(campaignDAO.getAllCampaigns().isEmpty() || forceUpdate) {
                    val result = campaignRepository.getAllCampaigns()
                    if(result.isSuccess) {
                        _uiState.value = _uiState.value.copy(campaigns = campaignDAO.getAllCampaigns())
                    }else {
                        _uiState.value = _uiState.value.copy(error = result.getOrNull())
                    }
                }else {
                    _uiState.value = _uiState.value.copy(campaigns = campaignDAO.getAllCampaigns())
                }
            } catch(e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun onCampaignSelected(campaignID: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CampaignDetails.route + "/$campaignID")
        }
    }

    fun onCreateSelected() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CreateCampaign.route)
        }
    }

    fun onInviteSelected() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.EnterCampaign.route)
        }
    }
}