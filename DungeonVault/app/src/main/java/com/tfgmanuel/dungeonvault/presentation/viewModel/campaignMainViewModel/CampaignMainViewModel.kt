package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CampaignMainState
import com.tfgmanuel.dungeonvault.timeStampValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CampaignMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val campaignDAO: CampaignDAO,
    private val navManager: NavManager,
    private val tokenManager: TokenManager,
    private val campaignRepository: CampaignRepository
) : ViewModel() {
    private  val _uiState = MutableStateFlow(CampaignMainState())
    val uiState: StateFlow<CampaignMainState> = _uiState.asStateFlow()

    private val campaignID: String? = savedStateHandle["campaignID"]

    init {
        load()
    }

    /**
     * Carga los detalles de la campaña desde la base de datos local.
     * También verifica si el usuario autenticado tiene permisos de edición (es el creador).
     */
    fun load() {
        viewModelScope.launch {
            if (campaignID != null) {
                val campaign = campaignDAO.getCampaignById(campaignID.toInt())
                val members = campaignDAO.getUsersByCampaign(campaignID.toInt())
                val userId = tokenManager.getUserID().firstOrNull()

                _uiState.value = _uiState.value.copy(
                    campaign = campaign,
                    creatorId = tokenManager.getUserID().first()!!,
                    members = members,
                    hasPermission = campaign!!.creator_id == userId
                )
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
                load()
            }
        }
    }

    /**
     * Muestra el cuadro de diálogo de confirmación.
     */
    fun showDialog() {
        _uiState.value = _uiState.value.copy(showDialog = true)
    }

    /**
     * Oculta el cuadro de diálogo de confirmación.
     */
    fun hideDialog() {
        _uiState.value = _uiState.value.copy(showDialog = false)
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

    fun onItemSelected(route: String) {
        viewModelScope.launch {
            if(route != Screen.CampaignMainScreen.route) {
                navManager.navigate(
                    route = route + "/${campaignID}",
                    popUpTo = route,
                    inclusive = true
                )
            }
        }
    }
}