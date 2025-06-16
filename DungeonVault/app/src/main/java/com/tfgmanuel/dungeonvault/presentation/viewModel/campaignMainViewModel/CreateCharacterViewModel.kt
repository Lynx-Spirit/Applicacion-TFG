package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.CharacterRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CreateCharacterState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val characterRepository: CharacterRepository,
    private val navManager: NavManager,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateCharacterState())
    val uiState: StateFlow<CreateCharacterState> = _uiState.asStateFlow()

    private val campaignID: String? = savedStateHandle["campaignID"]

    init {
        viewModelScope.launch {
            if (campaignID != null) {
                _uiState.value = _uiState.value.copy(
                    name = "",
                    description = "",
                    backstory = "",
                    imgUri = Uri.EMPTY,
                    visibility = false
                )
            }
        }
    }

    fun onValueChange(
        name: String,
        description: String,
        backstory: String,
        imgUri: Uri,
        visibility: Boolean
    ) {
        _uiState.value = _uiState.value.copy(
            name = name,
            description = description,
            backstory = backstory,
            imgUri = imgUri,
            visibility = visibility
        )
    }

    /**
     * Guarda el personaje y retrocede de pantalla.
     */
    fun onSave() {
        viewModelScope.launch {
            if (campaignID != null) {
                val result = characterRepository.createCharacter(
                    campaignID = campaignID.toInt(),
                    name = _uiState.value.name,
                    description = _uiState.value.description,
                    backstory = _uiState.value.backstory,
                    imgUri = _uiState.value.imgUri,
                    visibility = _uiState.value.visibility,
                    context = contextProvider.getContext()
                )

                if (result.isSuccess) {
                    navManager.navigate(
                        route = "${Screen.CampaignCharactersScreen.route}/$campaignID",
                        popUpTo = "${Screen.CampaignCharactersScreen.route}/$campaignID",
                        inclusive = true
                    )
                }
            }
        }
    }

    /**
     * Retrocede de pantalla.
     */
    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}