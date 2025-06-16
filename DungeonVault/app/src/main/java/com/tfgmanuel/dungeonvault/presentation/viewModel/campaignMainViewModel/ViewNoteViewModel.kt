package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.NoteDAO
import com.tfgmanuel.dungeonvault.data.repository.FileRepository
import com.tfgmanuel.dungeonvault.data.repository.NoteRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.presentation.states.ViewNoteState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de gestionar la vista de la vista de notas.
 */
@HiltViewModel
class ViewNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navManager: NavManager,
    private val noteDAO: NoteDAO,
    private val noteRepository: NoteRepository,
    private val fileRepository: FileRepository,
    private val contextProvider: ContextProvider,
    private  val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ViewNoteState())
    val uiState: StateFlow<ViewNoteState> = _uiState.asStateFlow()

    private val noteID: String? = savedStateHandle["noteID"]

    /**
     * Inicializa los datos de la nota.
     */
    init {
        viewModelScope.launch {
            if (noteID != null) {
                val note = noteDAO.getNote(noteID = noteID.toInt())
                val content = fileRepository.readTextFile(note.file_name)
                var readOnly = false

                if (note.user_id == 0 || tokenManager.getUserID().first()!! != note.user_id) {
                    readOnly = true
                }

                _uiState.value = _uiState.value.copy(
                    title = note.title,
                    newTitle = note.title,
                    readOnlyContent = readOnly,
                    content = content,
                    newContent = content,
                    visibility = note.visibility,
                    newVisibility = note.visibility
                )
            }
        }
    }

    /**
     * Modifica en caso de que haya habido algún cambio.
     *
     * @param title Nuevo título.
     * @param content Nuevo contenido.
     * @param visibility Nueva visibilidad.
     */
    fun onValueChange(title: String, content: String, visibility: Boolean) {
        _uiState.value = _uiState.value.copy(
            newTitle = title,
            newContent = content,
            newVisibility = visibility
        )
    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (noteID != null) {
                val note = noteDAO.getNote(noteID.toInt())

                val result = noteRepository.updateNote(
                    id = noteID.toInt(),
                    title = _uiState.value.newTitle,
                    content = _uiState.value.newContent,
                    fileName = note.file_name,
                    visibility = _uiState.value.newVisibility,
                    context = contextProvider.getContext()
                )

                if (result.isSuccess) {
                    navManager.goBack()
                }
            }
        }
    }

    fun showDialog() {
        _uiState.value = _uiState.value.copy(showDialog = true)
    }

    fun hideDialog() {
        _uiState.value = _uiState.value.copy(showDialog = false)
    }

    fun onDeleteClick() {
        viewModelScope.launch {
            if (noteID != null) {
                val result = noteRepository.deleteNote(noteID.toInt())

                if (result.isSuccess) {
                    navManager.goBack()
                }
            }
        }
    }

    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}