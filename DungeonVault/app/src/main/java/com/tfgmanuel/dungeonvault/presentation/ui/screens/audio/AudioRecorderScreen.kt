package com.tfgmanuel.dungeonvault.presentation.ui.screens.audio

import android.text.format.DateUtils.formatElapsedTime
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentation.ui.components.RequestPermission
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.audioViewModel.AudioViewModel

@Composable
fun AudioRecorderScreen(viewModel: AudioViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SecondaryTopBar(
                onBackClick = { viewModel.goBack() }
            )
        },
        content = { paddingValues ->
            RequestPermission(
                onPermissionGranted = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(PaddingValues()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (uiState.isRecording) "Grabando..." else "Pulsa para grabar",
                            fontSize = 32.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = formatElapsedTime(uiState.elapsedTime),
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        CustomButtonText(
                            onClick = { if (!uiState.isRecording) viewModel.startRecording() else viewModel.stopRecording() },
                            text = if (uiState.isRecording) "Detener" else "Grabar",
                        )
                    }
                },
                onPermissionDenied = {
                    Text(
                        modifier = Modifier.padding(paddingValues),
                        text = "No se puede grabar sin permisos."
                    )
                }
            )
        }
    )
}