package com.tfgmanuel.dungeonvault.presentation.viewmodel.campaignViewModel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.UpdateCampaignState
import com.tfgmanuel.dungeonvault.presentation.viewmodel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateCampaignViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val campaignDAO: CampaignDAO,
    private val campaignRepository: CampaignRepository,
    private val navManager: NavManager,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(UpdateCampaignState())
    val uiState: StateFlow<UpdateCampaignState> = _uiState.asStateFlow()

    private val campaignID: String? = savedStateHandle["campaignID"]

    init {
        viewModelScope.launch {
            if (campaignID != null) {
                val campaign = campaignDAO.getCampaignById(campaignID.toInt())
                if (campaign != null) {
                    _uiState.value = _uiState.value.copy(
                        title = campaign.title,
                        originalTitle = campaign.title,
                        description = campaign.description,
                        originalDescription = campaign.description,
                        imgName = campaign.img_name
                    )
                }
            }
        }
    }

    fun onValueChange(title: String, description: String, imgUri: Uri) {
        _uiState.value = _uiState.value.copy(
            title = title,
            description = description,
            imgUri = imgUri
        )
    }

    fun onSaveClick() {
        viewModelScope.launch {
            campaignID?.let {
                val result = campaignRepository.updateCampaign(
                    id = it.toInt(),
                    title = _uiState.value.title,
                    description = _uiState.value.description,
                    originalFileName = _uiState.value.imgName,
                    imgUri = _uiState.value.imgUri,
                    context = contextProvider.getContext()
                )

                if (result.isSuccess) {
                    navManager.navigate(
                        route = Screen.CampaignDetails.route + "/$campaignID",
                        popUpTo = Screen.CampaignDetails.route + "/$campaignID",
                        inclusive = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(error = result.getOrNull())
                }
            }
        }
    }

    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}