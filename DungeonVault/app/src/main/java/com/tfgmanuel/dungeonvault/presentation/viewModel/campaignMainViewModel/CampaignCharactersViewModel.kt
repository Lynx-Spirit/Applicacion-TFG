package com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.local.dao.CharacterDAO
import com.tfgmanuel.dungeonvault.data.repository.CharacterRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.CharacterSelectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de gesionar lo relacionado con los personajes.
 */
@HiltViewModel
class CampaignCharactersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navManager: NavManager,
    private val characterDAO: CharacterDAO,
    private val tokenManager: TokenManager,
    private val campaignDAO: CampaignDAO,
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharacterSelectionState())
    val uiState: StateFlow<CharacterSelectionState> = _uiState.asStateFlow()

    private val campaignID: String? = savedStateHandle["campaignID"]

    init {
        load()
    }

    /**
     * Se cargan los datos en el estado, en caso de que no hayan persoanjes se fuerza la actualiación para ver si hay.
     */
    fun load(update: Boolean = false) {
        viewModelScope.launch {
            if (campaignID != null) {
                val characters = characterDAO.getCampaignCharacters(campaignID.toInt())
                val campaign = campaignDAO.getCampaignById(campaignID.toInt())

                if (characters.isEmpty() && !update) {
                    forceUpdate()
                } else {
                    val userID = tokenManager.getUserID().first()!!
                    val creatorID = campaign!!.creator_id

                    _uiState.value = _uiState.value.copy(
                        characters = characters,
                        userID = userID,
                        creatorID = creatorID,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Se fuerza la actualuzación de todos los persoanjes.
     */
    fun forceUpdate() {
        viewModelScope.launch {
            if (campaignID != null) {
                val result = characterRepository.getAllCharacters(campaignID.toInt())

                if (result.isSuccess) {
                    load(true)
                }
            }
        }
    }

    /**
     * Visualziación de un personaje seleccioando.
     *
     * @param characterID Identificador del personaje seleccionado.
     */
    fun onCharacterSelected(characterID: Int) {
        viewModelScope.launch {
            navManager.navigate("${Screen.CampaignViewCharacterScreen.route}/$characterID")
        }
    }

    /**
     * Creación de un nuevo personaje.
     */
    fun onCreateSelected() {
        viewModelScope.launch {
            if (campaignID != null) {
                navManager.navigate("${Screen.CampaignNewCharacterScreen.route}/$campaignID")
            }
        }
    }

    /**
     * Navegación entre los distinos elementos del bottombar.
     */
    fun onItemSelected(route: String) {
        viewModelScope.launch {
            if(route != Screen.CampaignCharactersScreen.route) {
                navManager.navigate(
                    route = route + "/${campaignID}",
                    popUpTo = route,
                    inclusive = true
                )
            }
        }
    }
}