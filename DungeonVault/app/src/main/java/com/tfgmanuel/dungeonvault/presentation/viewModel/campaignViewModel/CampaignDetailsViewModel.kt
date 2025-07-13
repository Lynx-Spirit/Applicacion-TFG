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
import com.tfgmanuel.dungeonvault.timeStampValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de gestionar el estado de la vista de detalles de una campaña.
 *
 * Se encarga de:
 * - Cargar la información de la campaña desde la base de datos local.
 * - Verificar los permisos del usuario autenticado.
 * - Gestionar la navegación entre pantallas.
 * - Ejecutar acciones como editar, eliminar o abandonar una campaña.
 *
 * @param savedStateHandle Proporciona acceso a los argumentos de navegación (ej. el ID de la campaña).
 * @param campaignDAO Acceso a las campañas almacenadas localmente.
 * @param navManager Utilidad para controlar la navegación entre pantallas.
 * @param tokenManager Maneja y proporciona información sobre el usuario autenticado.
 * @param campaignRepository Repositorio que gestiona las operaciones de red y persistencia relacionadas con campañas.
 */
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

    /**
     * Carga los detalles de la campaña desde la base de datos local.
     * También verifica si el usuario autenticado tiene permisos de edición (es el creador).
     */
    private fun loadCampaign() {
        viewModelScope.launch {
            if (campaignID != null) {
                val campaign = campaignDAO.getCampaignById(campaignID.toInt())
                if (timeStampValid(campaign!!.lastUpdated)) {
                    forceUpdate()
                } else {
                    val members = campaignDAO.getUsersByCampaign(campaignID.toInt())

                    val userId = tokenManager.getUserID().firstOrNull()
                    _uiState.value = _uiState.value.copy(
                        campaign = campaign,
                        creatorId = tokenManager.getUserID().first()!!,
                        members = members,
                        hasPermission = campaign.creator_id == userId
                    )
                }
            }
        }
    }

    /**
     * Fuerza la actualización de la campaña.
     */
    fun forceUpdate() {
        viewModelScope.launch {
            if (campaignID != null) {
                val result = campaignRepository.getCampaign(campaignID.toInt())
                if (result.isSuccess) {
                    campaignRepository.getUsers(campaignID.toInt())
                }
                val campaign = campaignDAO.getCampaignById(campaignID.toInt())
                val members = campaignDAO.getUsersByCampaign(campaignID.toInt())

                val userId = tokenManager.getUserID().firstOrNull()
                _uiState.value = _uiState.value.copy(
                    campaign = campaign,
                    creatorId = tokenManager.getUserID().first()!!,
                    members = members,
                    hasPermission = campaign?.creator_id == userId
                )
            }
        }
    }

    /**
     * Muestra el cuadro de diálogo de confirmación.
     */
    fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    /**
     * Oculta el cuadro de diálogo de confirmación.
     */
    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    /**
     * Muestra el cuadro de diálogo de confirmación.
     */
    fun showKickDialog() {
        _uiState.value = _uiState.value.copy(showKickDialog = true)
    }

    /**
     * Oculta el cuadro de diálogo de confirmación.
     */
    fun hideKickDialog() {
        _uiState.value = _uiState.value.copy(showKickDialog = false)
    }

    /**
     * Muestra el cuadro de diálogo de confirmación.
     */
    fun showLeaveDialog() {
        _uiState.value = _uiState.value.copy(showLeaveDialog = true)
    }

    /**
     * Oculta el cuadro de diálogo de confirmación.
     */
    fun hideLeaveDialog() {
        _uiState.value = _uiState.value.copy(showLeaveDialog = false)
    }

    /**
     * Navega de regreso a la pantalla de selección de campaña.
     */
    fun goBack() {
        viewModelScope.launch {
            navManager.navigate(
                route = Screen.SelectCampaign.route,
                popUpTo = Screen.SelectCampaign.route,
                inclusive = true
            )
        }
    }

    /**
     * Acción para iniciar la campaña.
     */
    fun onStartClick() {
        viewModelScope.launch {
            navManager.navigate(
                route = Screen.CampaignMainScreen.route + "/${campaignID}",
                popUpTo = Screen.CampaignMainScreen.route + "/${campaignID}",
                inclusive = true
            )
        }
    }

    /**
     * Navega a la pantalla de edición de campaña.
     */
    fun onEditClick() {
        viewModelScope.launch {
            navManager.navigate(Screen.UpdateCampaign.route + "/$campaignID")
        }
    }

    /**
     * Elimina la campaña actual si el usuario tiene permiso.
     * Navega de regreso tras la eliminación o muestra un mensaje de error.
     */
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

    /**
     * Permite al usuario abandonar la campaña actual.
     * Navega de regreso tras el abandono o muestra un mensaje de error.
     */
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

    /**
     * Permite echar a un usuario seleccionado de la campaña
     *
     * @param userId Identificador del usuario a echar
     */
    fun onKickClick(userId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null)
            val result = campaignID?.let { campaignRepository.kickUser(campaignID.toInt(), userId) }

            if (result != null) {
                if (!result.isSuccess) {
                    _uiState.value = _uiState.value.copy(error = result.exceptionOrNull()?.message)
                }
            }
        }
    }
}