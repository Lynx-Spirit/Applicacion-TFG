package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomSwitch
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentation.ui.components.DecisionDialog
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.ViewNoteViewModel

@Composable
fun ViewNote(viewModel: ViewNoteViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SecondaryTopBar(
                onBackClick = { viewModel.goBack() }
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
                            value = uiState.newTitle,
                            onValueChange = {
                                viewModel.onValueChange(
                                    it,
                                    uiState.newContent,
                                    uiState.newVisibility
                                )
                            },
                            placeHolder = "Nombre de la nota"
                        )

                        HorizontalDivider(color = Color(0xFFE69141))

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.newContent,
                            textLabel = "CONTENIDO",
                            onValueChange = {
                                viewModel.onValueChange(
                                    uiState.newTitle,
                                    it,
                                    uiState.newVisibility
                                )
                            },
                            singleLine = false
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomSwitch(
                            information = "Visibilidad",
                            checked = uiState.newVisibility,
                            onValueChange = {
                                viewModel.onValueChange(
                                    uiState.newTitle,
                                    uiState.newContent,
                                    it
                                )
                            }
                        )

                        Row {
                            CustomButtonText(
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.showDialog() },
                                text = "Eliminar nota",
                                buttonColor = Color(0xFFF44336)
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            CustomButtonText(
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.onSaveClick() },
                                text = "Guardar cambios",
                                enabled = uiState.isFormValid
                            )
                        }

                        if (uiState.showDialog) {
                            DecisionDialog(
                                onDismissRequest = {
                                    viewModel.hideDialog()
                                },
                                onConfirmation = {
                                    viewModel.hideDialog()
                                    viewModel.onDeleteClick()
                                },
                                dialogTitle = "Eliminar Nota",
                                dialogText = "¿Estás seguro de que quieres eliminar esta nota? Esta acción no se puede deshacer."
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color(0XFF131313)
    )
}