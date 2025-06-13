package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CampaignMainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignCharactersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navManager: NavManager
) : ViewModel() {
    private  val _uiState = MutableStateFlow(CampaignMainState())
    val uiState: StateFlow<CampaignMainState> = _uiState.asStateFlow()

    private val campaignID: String? = savedStateHandle["campaignID"]

    init {
        load()
    }

    fun load() {

    }

    fun onItemSelected(route: String) {
        viewModelScope.launch {
            if(route != Screen.CampaignCharactersScreen.route) {
                navManager.navigate(
                    route = route + "/${campaignID}",
                    popUpTo = route,
                    inclusive = true
                )
            }
        }
    }
}