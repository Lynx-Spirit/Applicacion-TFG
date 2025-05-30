package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.CampaignRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.EnterCampaignState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de la lógica de la pantalla de ingreso a una campaña mediante código de invitación.
 *
 * Funcionalidades:
 * - Permite al usuario introducir un código de invitación.
 * - Realiza la solicitud para unirse a la campaña correspondiente.
 * - Gestiona navegación y errores.
 *
 * @param navManager Encargado de la navegación entre pantallas.
 * @param campaignRepository Repositorio encargado de manejar la lógica de inserción del usuario en una campaña.
 */
@HiltViewModel
class EnterCampaignViewModel @Inject constructor(
    private val navManager: NavManager,
    private val campaignRepository: CampaignRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EnterCampaignState())
    val uiState: StateFlow<EnterCampaignState> = _uiState.asStateFlow()

    /**
     * Actualiza el código de invitación introducido por el usuario.
     * El código se convierte a mayúsculas y se limita a 6 caracteres.
     *
     * @param inviteCode Código de invitación ingresado por el usuario.
     */
    fun onInviteChange(inviteCode: String) {
        if(inviteCode.length <= 6) {
            _uiState.value = _uiState.value.copy(
                inviteCode = inviteCode.uppercase()
            )
        }
    }

    /**
     * Lógica para unirse a una campaña mediante el código de invitación actual.
     * Si la operación tiene éxito, navega de vuelta a la pantalla de selección de campaña.
     * En caso de error, muestra un mensaje de error en el estado.
     */
    fun onInviteSelected() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null)
            val result =
                campaignRepository.insertUser(uiState.value.inviteCode.uppercase())
            if (result.isSuccess) {
                navManager.navigate(
                    route = Screen.SelectCampaign.route,
                    popUpTo = Screen.SelectCampaign.route,
                    inclusive = true
                )
            } else {
                val errorMessage =
                    result.exceptionOrNull()?.message ?: "Error al unirse a la partida."
                _uiState.value = _uiState.value.copy(error = errorMessage)
            }
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