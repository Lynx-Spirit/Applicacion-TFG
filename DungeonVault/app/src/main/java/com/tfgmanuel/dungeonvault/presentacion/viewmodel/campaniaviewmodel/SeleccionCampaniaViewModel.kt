package com.tfgmanuel.dungeonvault.presentacion.viewmodel.campaniaviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.repository.CampaniaRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentacion.states.SeleccionCamapaniaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeleccionCampaniaViewModel @Inject constructor(
    private val navigationManager: NavManager,
    private val campaniaRepository: CampaniaRepository,
    private val campaignDAO: CampaignDAO
): ViewModel() {
    private val _uistate = MutableStateFlow(SeleccionCamapaniaState())
    val uistate: StateFlow<SeleccionCamapaniaState> = _uistate.asStateFlow()

    init {
        loadCampaigns()
    }

    fun loadCampaigns() {
        _uistate.value = _uistate.value.copy(error = null)
        viewModelScope.launch {
            try {
                if(campaignDAO.getAllCampaigns().isEmpty()) {
                    val resultado = campaniaRepository.getAllCampaigns()
                    if(resultado.isSuccess) {
                        _uistate.value = _uistate.value.copy(campanias = campaignDAO.getAllCampaigns())
                    }else {
                        _uistate.value = _uistate.value.copy(error = resultado.getOrNull())
                    }
                }else {
                    _uistate.value = _uistate.value.copy(campanias = campaignDAO.getAllCampaigns())
                }
            } catch(e: Exception) {
                _uistate.value = _uistate.value.copy(error = e.message)
            }
        }
    }

    fun onCampaignSelected(campaignID: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.DetalleCampania.route + "/$campaignID")
        }
    }

    fun onCreateSelected() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CrearCampania.route)
        }
    }

    fun onInviteSelected() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.EntrarCampania.route)
        }
    }
}