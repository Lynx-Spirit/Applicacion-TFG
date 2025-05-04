package com.tfgmanuel.dungeonvault.presentacion.viewmodel.campaniaviewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.repository.CampaniaRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentacion.states.CrearCampaniaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NuevaCampaniaViewModel @Inject constructor(
    private val navManager: NavManager,
    private val campaignRepository: CampaniaRepository
): ViewModel() {
    private val _uistate = MutableStateFlow(CrearCampaniaState())
    val uistate: StateFlow<CrearCampaniaState> = _uistate.asStateFlow()

    fun onCreateChanged(titulo: String, description: String, uri: Uri) {
        _uistate.value = _uistate.value.copy(
            titulo = titulo, description = description, imgUri = uri
        )
    }

    fun createCampaign(context: Context) {
        viewModelScope.launch {
            campaignRepository.create(
                title = _uistate.value.titulo,
                description = _uistate.value.description,
                imgUri = _uistate.value.imgUri,
                context = context
            )
            navManager.navigate(Screen.SeleccionCampania.route)
        }
    }

    fun goBack() {
        viewModelScope.launch {
            navManager.navigate(Screen.SeleccionCampania.route)
        }
    }
}