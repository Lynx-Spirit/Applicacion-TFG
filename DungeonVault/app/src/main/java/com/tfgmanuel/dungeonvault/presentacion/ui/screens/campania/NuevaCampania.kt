package com.tfgmanuel.dungeonvault.presentacion.ui.screens.campania

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.presentacion.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentacion.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentacion.ui.components.ImageSelector
import com.tfgmanuel.dungeonvault.presentacion.ui.components.TopBarInvitacion
import com.tfgmanuel.dungeonvault.presentacion.viewmodel.campaniaviewmodel.NuevaCampaniaViewModel


@Composable
fun NuevaCampania(modifier: Modifier = Modifier, viewModel: NuevaCampaniaViewModel) {
    val uiState by viewModel.uistate.collectAsState()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBarInvitacion(
                onBackClick = {
                    viewModel.goBack()
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = "Crear campaña",
                        color = Color.White,
                        fontSize = 32.sp
                    )

                    HorizontalDivider(color = Color(0xFFE69141))

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.titulo,
                        textLabel = "TÍTULO",
                        onValueChange = {
                            viewModel.onCreateChanged(
                                it,
                                uiState.description,
                                uiState.imgUri
                            )
                        }
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.description,
                        textLabel = "DESCRIPCIÓN",
                        onValueChange = {
                            viewModel.onCreateChanged(
                                uiState.titulo,
                                it,
                                uiState.imgUri
                            )
                        },
                        singleLine = false
                    )

                    ImageSelector(
                        uri = uiState.imgUri,
                        onImageSelected = { uri ->
                            if (uri != null) {
                                viewModel.onCreateChanged(
                                    titulo = uiState.titulo,
                                    description = uiState.description,
                                    uri = uri
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomButtonText(
                        onClick = { viewModel.createCampaign(context) },
                        text = "Crear partida",
                        enabled = (uiState.titulo.isNotBlank() && uiState.description.isNotBlank())
                    )
                }
            }
        },
        containerColor = Color(0xFF131313)
    )
}