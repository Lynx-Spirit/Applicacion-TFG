package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.repository.NoteRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.NewNoteState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Se encarga del controlo de la pantalla de la creación de las nuevas notas.
 * En concreto de gestionar el estado, la navegación y el almacenaje.
 */
@HiltViewModel
class NewNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navManager: NavManager,
    private val noteRepository: NoteRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewNoteState())
    val uiState: StateFlow<NewNoteState> = _uiState.asStateFlow()

    private val campaignID: String? = savedStateHandle["campaignID"]

    init {
        _uiState.value = _uiState.value.copy(
            title = "",
            content = "",
            visibility = false
        )
    }

    /**
     * Actualización de los valores del estado de la UI
     *
     * @param title Nuevo valor del título.
     * @param content Nuevo contenido de la nota.
     * @param visibility Nueva visibilidad de la nota.
     */
    fun onValueChange(title: String, content: String, visibility: Boolean) {
        _uiState.value = _uiState.value.copy(
            title = title,
            content = content,
            visibility = visibility
        )
    }

    /**
     * Almacenaje de la nota creada por el jugador.
     */
    fun onSaveClick() {
        viewModelScope.launch {
            if (campaignID != null) {
                val result = noteRepository.createNote(
                    campaignID = campaignID.toInt(),
                    title = _uiState.value.title,
                    content = _uiState.value.content,
                    visibility = _uiState.value.visibility,
                    context = contextProvider.getContext()
                )

                if (result.isSuccess) {
                    navManager.navigate(
                        route = Screen.CampaignNotesScreen.route + "/$campaignID",
                        popUpTo = Screen.CampaignNotesScreen.route + "/$campaignID",
                        inclusive = true
                    )
                }
            }
        }
    }

    /**
     * Ir hacia atrás
     */
    fun onBackClicked() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}