package com.tfgmanuel.dungeonvault.presentation.viewModel.audioViewModel

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.audios.RecordingService
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.states.AudioState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Viewmodel encarfado de gestionar todo lo relacionado con la parte de la grabación de los audios.
 */
@HiltViewModel
class AudioViewModel @Inject constructor(
    private val application: Application,
    private val navManager: NavManager,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(AudioState())
    val uiState: StateFlow<AudioState> = _uiState.asStateFlow()
    private val campaignID: String? = savedStateHandle["campaignID"]
    private var timerJob: Job? = null

    /**
     * Inicio de la grabación
     */
    fun startRecording() {
        val intent = Intent(application, RecordingService::class.java).apply {
            var id = -1
            if (campaignID != null) {
                id = campaignID.toInt()
            }
            putExtra("campaign_id", id)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.startForegroundService(intent)
        } else {
            application.startService(intent)
        }

        _uiState.value = _uiState.value.copy(isRecording = true)
        startTimer()
    }

    /**
     * Finalización de la grabación
     */
    fun stopRecording() {
        viewModelScope.launch {
            val intent = Intent(application, RecordingService::class.java)
            application.stopService(intent)

            _uiState.value = _uiState.value.copy(isRecording = false)
            stopTimer()
            navManager.navigate(
                route = "${Screen.CampaignNotesScreen.route}/$campaignID",
                popUpTo = "${Screen.CampaignNotesScreen.route}/$campaignID",
                inclusive = true
            )
        }
    }

    /**
     * Comienza el timer
     */
    private fun startTimer() {
        _uiState.value = _uiState.value.copy(elapsedTime = 0L)
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.isRecording) {
                delay(1000)
                _uiState.value = _uiState.value.copy(elapsedTime = (_uiState.value.elapsedTime + 1))
            }
        }
    }

    /**
     * Termina el timer
     */
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun goBack() {
        viewModelScope.launch {
            if (!_uiState.value.isRecording) {
                navManager.goBack()
            }
        }
    }

}