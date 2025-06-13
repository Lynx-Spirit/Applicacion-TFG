package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignNotesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navManager: NavManager
) : ViewModel() {
    private val campaignID: String? = savedStateHandle["campaignID"]

    init {
        load()
    }

    fun load() {

    }

    fun onItemSelected(route: String) {
        viewModelScope.launch {
            if(route != Screen.CampaignNotesScreen.route) {
                navManager.navigate(
                    route = route + "/${campaignID}",
                    popUpTo = route,
                    inclusive = true
                )
            }
        }
    }
}