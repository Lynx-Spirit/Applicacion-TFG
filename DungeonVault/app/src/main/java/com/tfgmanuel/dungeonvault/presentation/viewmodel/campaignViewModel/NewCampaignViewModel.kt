package com.tfgmanuel.dungeonvault.presentation.viewmodel.campaignViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CampaignState
import com.tfgmanuel.dungeonvault.presentation.viewmodel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewCampaignViewModel @Inject constructor(
    private val navManager: NavManager,
    private val campaignRepository: CampaignRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(CampaignState())
    val uiState: StateFlow<CampaignState> = _uiState.asStateFlow()

    fun onCreateChanged(title: String, description: String, uri: Uri) {
        _uiState.value = _uiState.value.copy(
            title = title, description = description, imgUri = uri
        )
    }

    fun createCampaign() {
        viewModelScope.launch {
            campaignRepository.createCampaign(
                title = _uiState.value.title,
                description = _uiState.value.description,
                imgUri = _uiState.value.imgUri,
                context = contextProvider.getContext()
            )
            navManager.navigate(
                route = Screen.SelectCampaign.route,
                popUpTo = Screen.SelectCampaign.route,
                inclusive = true
            )
        }
    }

    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}