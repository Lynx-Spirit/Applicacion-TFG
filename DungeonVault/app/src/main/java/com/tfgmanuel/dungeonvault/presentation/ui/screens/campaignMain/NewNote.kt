package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomSwitch
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.NewNoteViewModel

@Composable
fun NewNote(viewModel: NewNoteViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SecondaryTopBar(
                onBackClick = { viewModel.onBackClicked() }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn (
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {

                        CustomTextField(
                            value = uiState.title,
                            onValueChange = {
                                viewModel.onValueChange(
                                    it,
                                    uiState.content,
                                    uiState.visibility
                                )
                            },
                            placeHolder = "Nueva nota"
                        )

                        HorizontalDivider(color = Color(0xFFE69141))

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.content,
                            textLabel = "CONTENIDO",
                            onValueChange = {
                                viewModel.onValueChange(
                                    uiState.title,
                                    it,
                                    uiState.visibility
                                )
                            },
                            singleLine = false
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomSwitch(
                            information = "Visibilidad",
                            checked = uiState.visibility,
                            onValueChange = {
                                viewModel.onValueChange(
                                    uiState.title,
                                    uiState.content,
                                    it
                                )
                            }
                        )

                        CustomButtonText(
                            onClick = { viewModel.onSaveClick() },
                            text = "Crear nota",
                            enabled = uiState.isFormValid
                        )
                    }
                }
            }
        },
        containerColor = Color(0XFF131313)
    )
}