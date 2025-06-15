package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.local.dao.NoteDAO
import com.tfgmanuel.dungeonvault.data.repository.NoteRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CampaignNotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignNotesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navManager: NavManager,
    private val campaignDAO: CampaignDAO,
    private val noteDAO: NoteDAO,
    private val tokenManager: TokenManager,
    private val noteRepository: NoteRepository
) : ViewModel() {
    private  val _uiState = MutableStateFlow(CampaignNotesState())
    val uiState: StateFlow<CampaignNotesState> = _uiState.asStateFlow()

    private val campaignID: String? = savedStateHandle["campaignID"]

    /**
     * Inicializa el viewModel obteniendo todas las notas de la campaña.
     */
    init {
        load()
    }

    /**
     * Carga las notas de la campaña.
     */
    fun load() {
        viewModelScope.launch {
            if (campaignID != null) {
                val campaign = campaignDAO.getCampaignById(campaignID.toInt())
                val notes = noteDAO.getCampaignNotes(campaignID.toInt())

                _uiState.value = _uiState.value.copy(
                    notes = notes,
                    userID = tokenManager.getUserID().first()!!,
                    creatorID = campaign!!.creator_id
                )
            }

        }
    }

    /**
     * Fuerza la actualización de las notas de la campaña.
     */
    fun forceUpdate() {
        viewModelScope.launch {
            if (campaignID != null) {
                noteRepository.getAllNotes(campaignID.toInt())
                load()
            }
        }
    }

    /**
     * Creación de una nueva nota.
     */
    fun onCreateNoteSelected() {
        viewModelScope.launch {
            navManager.navigate(
                route = Screen.CampaignNewNote.route + "/${campaignID}"
            )
        }
    }

    /**
     * Inicio de una nueva transcripcion.
     */
    fun onCreateTranscription() {
        //Para hacerlo creo que tendríamos llamar a la api o algo apra indicar que iniciamos transcripción
    }

    /**
     * Selección de una nota concreta.
     *
     * @param id Identificador de la nota seleccionada.
     */
    fun onNoteSelected(noteID: Int) {
        viewModelScope.launch {
            navManager.navigate(
                route = Screen.CampaignViewNote.route + "/${noteID}"
            )
        }
    }

    fun onItemSelected(route: String) {
        viewModelScope.launch {
            if(route != Screen.CampaignNotesScreen.route) {
                navManager.navigate(
                    route = route + "/${campaignID}",
                    popUpTo = route,
                    inclusive = true
                )
            }
        }
    }
}