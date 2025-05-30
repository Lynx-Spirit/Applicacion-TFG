package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.UpdateCampaignState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de gestionar la lógica de actualización de campañas existentes.
 *
 * Funcionalidades principales:
 * - Carga los datos de una campaña seleccionada para edición.
 * - Permite al usuario modificar título, descripción e imagen.
 * - Llama al repositorio para persistir los cambios.
 * - Controla la navegación al finalizar o cancelar la edición.
 *
 * @param savedStateHandle Contiene el ID de la campaña recibido desde la navegación.
 * @param campaignDAO Acceso local a los datos de la campaña.
 * @param campaignRepository Repositorio para operaciones remotas o persistentes de campaña.
 * @param navManager Gestor de navegación de pantallas.
 * @param contextProvider Proveedor del contexto de aplicación para operaciones que lo requieren.
 */
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

    /**
     * Actualiza el estado de la campaña cuando el usuario cambia los datos en el formulario.
     *
     * @param title Nuevo título de la campaña.
     * @param description Nueva descripción de la campaña.
     * @param imgUri URI de la nueva imagen seleccionada por el usuario.
     */
    fun onValueChange(title: String, description: String, imgUri: Uri) {
        _uiState.value = _uiState.value.copy(
            title = title,
            description = description,
            imgUri = imgUri
        )
    }

    /**
     * Guarda los cambios realizados en la campaña.
     *
     * - Llama al repositorio para persistir los nuevos datos.
     * - Si la operación es exitosa, redirige a los detalles de la campaña.
     * - En caso de error, actualiza el estado con el mensaje de error.
     */
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

    /**
     * Acción para regresar a la pantalla anterior sin guardar los cambios.
     */
    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}