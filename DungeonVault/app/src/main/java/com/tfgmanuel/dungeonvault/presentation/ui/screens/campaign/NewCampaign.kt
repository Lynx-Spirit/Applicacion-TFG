package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentation.ui.components.ImageSelector
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.NewCampaignViewModel


@Composable
fun NewCampaign(modifier: Modifier = Modifier, viewModel: NewCampaignViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            SecondaryTopBar(
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
                LazyColumn (
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 0.dp),
                            text = "Crear campaña",
                            color = Color.White,
                            fontSize = 32.sp
                        )

                        HorizontalDivider(color = Color(0xFFE69141))

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.title,
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
                                    uiState.title,
                                    it,
                                    uiState.imgUri
                                )
                            },
                            singleLine = false
                        )

                        ImageSelector(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            uri = uiState.imgUri,
                            defaultImage = R.drawable.sinimg,
                            onImageSelected = { uri ->
                                if (uri != null) {
                                    viewModel.onCreateChanged(
                                        title = uiState.title,
                                        description = uiState.description,
                                        uri = uri
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomButtonText(
                            onClick = { viewModel.createCampaign() },
                            text = "Crear partida",
                            enabled = uiState.isFormValid
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF131313)
    )
}