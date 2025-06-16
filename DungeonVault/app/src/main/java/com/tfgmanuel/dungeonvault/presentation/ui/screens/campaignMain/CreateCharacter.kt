package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomSwitch
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentation.ui.components.ImageSelector
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CreateCharacterViewModel

@Composable
fun CreateCharacter(viewModel: CreateCharacterViewModel) {
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
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 0.dp),
                            text = "Nuevo personaje",
                            color = Color.White,
                            fontSize = 32.sp
                        )

                        HorizontalDivider(color = Color(0xFFE69141))

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        Row (
                            modifier = Modifier.fillMaxWidth(0.9f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ImageSelector(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .height(100.dp)
                                    .clip(CircleShape),
                                uri = uiState.imgUri,
                                defaultImage = R.drawable.default_avatar,
                                onImageSelected = { uri ->
                                    if (uri != null) {
                                        viewModel.onValueChange(
                                            name = uiState.name,
                                            description = uiState.description,
                                            backstory = uiState.backstory,
                                            imgUri = uri,
                                            visibility = uiState.visibility
                                        )
                                    }
                                }
                            )

                            Spacer(Modifier.width(10.dp))

                            CustomTextField(
                                modifier = Modifier.weight(0.5f),
                                value = uiState.name,
                                textLabel = "NOMBRE",
                                onValueChange = {
                                    viewModel.onValueChange(
                                        name = it,
                                        description = uiState.description,
                                        backstory = uiState.backstory,
                                        imgUri = uiState.imgUri,
                                        visibility = uiState.visibility,
                                    )
                                },
                                singleLine = true
                            )
                        }

                    }

                    item {

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.description,
                            textLabel = "DESCRIPCIÓN",
                            onValueChange = {
                                viewModel.onValueChange(
                                    name = uiState.name,
                                    description = it,
                                    backstory = uiState.backstory,
                                    imgUri = uiState.imgUri,
                                    visibility = uiState.visibility,
                                )
                            },
                            singleLine = false
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.backstory,
                            textLabel = "BACKSTORY",
                            onValueChange = {
                                viewModel.onValueChange(
                                    name = uiState.name,
                                    description = uiState.description,
                                    backstory = it,
                                    imgUri = uiState.imgUri,
                                    visibility = uiState.visibility,
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
                                    name = uiState.name,
                                    description = uiState.description,
                                    backstory = uiState.backstory,
                                    imgUri = uiState.imgUri,
                                    visibility = it,
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomButtonText(
                            onClick = { viewModel.onSave() },
                            text = "Crear personaje",
                            enabled = uiState.isFormValid
                        )
                    }
                }
            }
        },
        containerColor = Color(0XFF131313)
    )
}