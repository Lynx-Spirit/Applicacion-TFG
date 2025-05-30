package com.tfgmanuel.dungeonvault.presentation.viewModel.otherViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.UserDataStore
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.presentation.states.UpdateUserState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de gestionar el estado y la lógica de la pantalla
 * de actualización de la información del usuario.
 *
 * Esta clase se encarga de obtener los datos actuales del usuario,
 * actualizar el nombre de usuario y el avatar, y manejar la navegación
 * de regreso después de guardar los cambios.
 *
 * @property userDataStore Almacenamiento local para datos persistentes del usuario.
 * @property authRepository Repositorio para operaciones de autenticación con backend.
 * @property navManager Administrador de navegación de pantallas.
 * @property contextProvider Proveedor de contexto para operaciones como carga de imágenes.
 */
@HiltViewModel
class UpdateUserInfoViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val authRepository: AuthRepository,
    val navManager: NavManager,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(UpdateUserState())
    val uiState: StateFlow<UpdateUserState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = authRepository.getUser()

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    originalNickname = userDataStore.getNickname().first()!!,
                    nickname = userDataStore.getNickname().first()!!,
                    originalAvatar = userDataStore.getAvatar().first()!!
                )
            }
        }
    }

    /**
     * Actualiza los valores del estado con el nuevo apodo y avatar seleccionados.
     *
     * @param nickname Nuevo nombre del usuario.
     * @param avatarUri URI del nuevo avatar.
     */
    fun onUpdateChanged(nickname: String, avatarUri: Uri) {
        _uiState.value = _uiState.value.copy(
            nickname = nickname,
            avatarUri = avatarUri
        )
    }

    /**
     * Guarda los cambios realizados por el usuario y regresa a la pantalla anterior.
     *
     * Llama al repositorio para aplicar la actualización en el backend. Si es exitoso,
     * se ejecuta la navegación hacia atrás.
     */
    fun onSaveClick() {
        viewModelScope.launch {
            val result = authRepository.update(
                nickname = _uiState.value.nickname,
                avatarUri = _uiState.value.avatarUri,
                context = contextProvider.getContext()
            )

            if (result.isSuccess) {
                goBack()
            }
        }
    }

    /**
     * Regresa a la pantalla anterior mediante el administrador de navegación.
     */
    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}