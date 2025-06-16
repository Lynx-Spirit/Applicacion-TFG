package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CharacterDAO
import com.tfgmanuel.dungeonvault.data.repository.CharacterRepository
import com.tfgmanuel.dungeonvault.data.repository.FileRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.ViewCharacterState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de gestionar la vista y modificación de los personajes de la campaña.
 */
@HiltViewModel
class ViewCharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navManager: NavManager,
    private val characterDAO: CharacterDAO,
    private val characterRepository: CharacterRepository,
    private val fileRepository: FileRepository,
    private val contextProvider: ContextProvider,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ViewCharacterState())
    val uiState: StateFlow<ViewCharacterState> = _uiState.asStateFlow()

    private val characterID: String? = savedStateHandle["characterID"]

    /**
     * Inicializa los datos del personaje.
     */
    init {
        viewModelScope.launch {
            if (characterID != null) {
                val character = characterDAO.getCharacter(characterID.toInt())
                val backstory = fileRepository.readTextFile(character.filename_backstory)
                var readOnly = false

                if (tokenManager.getUserID().first()!! != character.user_id) {
                    readOnly = true
                }

                _uiState.value = _uiState.value.copy(
                    name = character.name,
                    newName = character.name,
                    description = character.description,
                    newDescription = character.description,
                    backstory = backstory,
                    newBackstory = backstory,
                    originalImg = character.img_name,
                    imgUri = Uri.EMPTY,
                    visibility = character.visibility,
                    newVisibility = character.visibility,
                    readOnly = readOnly
                )
            }
        }
    }

    /**
     * Modifica el estado en caso de que haya habido un cambio
     *
     * @param name Nuevo nombre del personaje.
     * @param description Nueva descripción del personaje.
     * @param backStory Nuevo backstory del personaje.
     * @param imgUri Nueva imagen del perosnaje.
     * @param visibility Nueva visibilidad del personaje.
     */
    fun onValueChange(
        name: String,
        description: String,
        backStory: String,
        imgUri: Uri,
        visibility: Boolean
    ) {
        _uiState.value = _uiState.value.copy(
            newName = name,
            newDescription = description,
            newBackstory = backStory,
            imgUri = imgUri,
            newVisibility = visibility
        )
    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (characterID != null) {
                val character = characterDAO.getCharacter(characterID.toInt())

                val result = characterRepository.updateCharacter(
                    id = characterID.toInt(),
                    name = _uiState.value.newName,
                    description = _uiState.value.newDescription,
                    backstory = _uiState.value.newBackstory,
                    backstoryFilename = character.filename_backstory,
                    imgUri = _uiState.value.imgUri,
                    visibility = _uiState.value.newVisibility,
                    context = contextProvider.getContext()
                )

                if (result.isSuccess) {
                    navManager.navigate(
                        route = "${Screen.CampaignCharactersScreen.route}/${character.campaign_id}",
                        popUpTo = "${Screen.CampaignCharactersScreen.route}/${character.campaign_id}",
                        inclusive = true
                    )
                }
            }
        }
    }

    fun onDeleteClick() {
        viewModelScope.launch {
            if (characterID != null) {
                val character = characterDAO.getCharacter(characterID.toInt())
                val result = characterRepository.deleteCharacter(characterID.toInt())

                if (result.isSuccess) {
                    navManager.navigate(
                        route = "${Screen.CampaignCharactersScreen.route}/${character.campaign_id}",
                        popUpTo = "${Screen.CampaignCharactersScreen.route}/${character.campaign_id}",
                        inclusive = true
                    )
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

    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}