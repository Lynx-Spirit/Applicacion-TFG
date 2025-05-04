package com.tfgmanuel.dungeonvault.presentacion.viewmodel.campaniaviewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallesCampaniaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val campaignDAO: CampaignDAO,
    private val navManager: NavManager,
    private val tokenManager: TokenManager
): ViewModel(){
    private val campaignID: String? = savedStateHandle["campaignID"]
    val campaign = mutableStateOf<Campaign?>(null)

    init {
        viewModelScope.launch {
            if(campaignID != null) {
                campaign.value = campaignDAO.getCampaignById(campaignID.toInt())
            }
        }
    }

    fun goBack() {
        viewModelScope.launch {
            navManager.navigate(Screen.SeleccionCampania.route)
        }
    }

    fun checkPermission(): Boolean {
        var result = false
        viewModelScope.launch {
            result = campaign.value!!.creator_id == tokenManager.getUserID().firstOrNull()
        }
        return result
    }
}