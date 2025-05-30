package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CampaignState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de gestionar la creación de una nueva campaña.
 *
 * Funcionalidades:
 * - Mantiene el estado de los datos del formulario de creación.
 * - Interactúa con el repositorio para crear una nueva campaña.
 * - Controla la navegación posterior a la creación.
 *
 * @param navManager Encargado de la navegación entre pantallas.
 * @param campaignRepository Repositorio que gestiona las operaciones relacionadas con campañas.
 * @param contextProvider Proveedor de contexto para acceder a recursos como imágenes.
 */
@HiltViewModel
class NewCampaignViewModel @Inject constructor(
    private val navManager: NavManager,
    private val campaignRepository: CampaignRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(CampaignState())
    val uiState: StateFlow<CampaignState> = _uiState.asStateFlow()

    /**
     * Actualiza los valores del estado de la campaña conforme el usuario los introduce.
     *
     * @param title Título de la campaña introducido por el usuario.
     * @param description Descripción de la campaña.
     * @param uri URI de la imagen seleccionada para la campaña.
     */
    fun onCreateChanged(title: String, description: String, uri: Uri) {
        _uiState.value = _uiState.value.copy(
            title = title, description = description, imgUri = uri
        )
    }

    /**
     * Llama al repositorio para crear una nueva campaña usando los datos actuales del estado.
     * Si se crea correctamente, redirige al usuario a la pantalla de selección de campañas.
     */
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

    /**
     * Acción para volver a la pantalla anterior.
     */
    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}