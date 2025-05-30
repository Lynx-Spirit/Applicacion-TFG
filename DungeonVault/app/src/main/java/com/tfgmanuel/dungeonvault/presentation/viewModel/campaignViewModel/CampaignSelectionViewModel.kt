package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel

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

/**
 * ViewModel responsable de gestionar la lógica de la pantalla de selección de campaña.
 *
 * Funcionalidades clave:
 * - Carga las campañas desde la base de datos local o desde el repositorio remoto si es necesario.
 * - Gestiona la navegación a otras pantallas como detalles, creación o unión a una campaña.
 *
 * @param navigationManager Controlador de navegación utilizado para moverse entre pantallas.
 * @param campaignRepository Fuente de datos remota para obtener campañas del servidor.
 * @param campaignDAO Acceso a campañas almacenadas localmente en la base de datos.
 */
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

    /**
     * Carga la lista de campañas.
     * Si no hay campañas locales o se fuerza la actualización, intenta recuperar las campañas del servidor.
     *
     * @param forceUpdate Si es true, siempre consulta al servidor, ignorando los datos locales.
     */
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

    /**
     * Maneja la acción cuando el usuario selecciona una campaña de la lista.
     * Navega a la pantalla de detalles de la campaña.
     *
     * @param campaignID ID de la campaña seleccionada.
     */
    fun onCampaignSelected(campaignID: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CampaignDetails.route + "/$campaignID")
        }
    }

    /**
     * Navega a la pantalla de creación de una nueva campaña.
     */
    fun onCreateSelected() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.CreateCampaign.route)
        }
    }

    /**
     * Navega a la pantalla para unirse a una campaña mediante un código de invitación.
     */
    fun onInviteSelected() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.EnterCampaign.route)
        }
    }
}