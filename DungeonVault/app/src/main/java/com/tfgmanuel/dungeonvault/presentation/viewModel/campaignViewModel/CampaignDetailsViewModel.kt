package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CampaignDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val campaignDAO: CampaignDAO,
    private val navManager: NavManager,
    private val tokenManager: TokenManager,
    private val campaignRepository: CampaignRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CampaignDetailsState())
    val uiState: StateFlow<CampaignDetailsState> = _uiState.asStateFlow()

    private val campaignID: String? = savedStateHandle["campaignID"]

    init {
        loadCampaign()
    }

    private fun loadCampaign() {
        viewModelScope.launch {
            if (campaignID != null) {
                val campaign = campaignDAO.getCampaignById(campaignID.toInt())
                val userId = tokenManager.getUserID().firstOrNull()
                _uiState.value = _uiState.value.copy(
                    campaign = campaign,
                    hasPermission = campaign?.creator_id == userId
                )
            }
        }
    }

    fun showDialog() {
        _uiState.value = _uiState.value.copy(showDialog = true)
    }

    fun hideDialog() {
        _uiState.value = _uiState.value.copy(showDialog = false)
    }

    fun goBack() {
        viewModelScope.launch {
            navManager.navigate(
                route = Screen.SelectCampaign.route,
                popUpTo = Screen.SelectCampaign.route,
                inclusive = true
            )
        }
    }

    fun onStartClick() {
        //Llamada al navManager
    }

    fun onEditClick() {
        viewModelScope.launch {
            navManager.navigate(Screen.UpdateCampaign.route + "/$campaignID")
        }
    }

    fun onDeleteClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null)
            val result = campaignID?.let { campaignRepository.removeCampaign(it.toInt()) }

            if (result != null) {
                if (result.isSuccess) {
                    navManager.navigate(
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

    fun onAbandonClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null)
            val result = campaignID?.let { campaignRepository.removeUser(it.toInt()) }

            if (result != null) {
                if (result.isSuccess) {
                    navManager.navigate(
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
}