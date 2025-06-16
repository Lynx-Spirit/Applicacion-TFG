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
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomImage
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomSwitch
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentation.ui.components.DecisionDialog
import com.tfgmanuel.dungeonvault.presentation.ui.components.ImageSelector
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.ViewCharacterViewModel

@Composable
fun ViewCharacter(viewModel: ViewCharacterViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            SecondaryTopBar(
                onBackClick = { viewModel.goBack() }
            )
        },
        content = {paddingValues ->
            if (!uiState.readOnly) {
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
                                text = "Modificar personaje",
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
                                    imageName = uiState.originalImg,
                                    onImageSelected = { uri ->
                                        if (uri != null) {
                                            viewModel.onValueChange(
                                                name = uiState.newName,
                                                description = uiState.newDescription,
                                                backStory = uiState.newBackstory,
                                                imgUri = uri,
                                                visibility = uiState.newVisibility
                                            )
                                        }
                                    }
                                )

                                Spacer(Modifier.width(10.dp))

                                CustomTextField(
                                    modifier = Modifier.weight(0.5f),
                                    value = uiState.newName,
                                    textLabel = "NOMBRE",
                                    onValueChange = {
                                        viewModel.onValueChange(
                                            name = it,
                                            description = uiState.newDescription,
                                            backStory = uiState.newBackstory,
                                            imgUri = uiState.imgUri,
                                            visibility = uiState.newVisibility,
                                        )
                                    },
                                    singleLine = true
                                )
                            }

                        }

                        item {

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = uiState.newDescription,
                                textLabel = "DESCRIPCIÓN",
                                onValueChange = {
                                    viewModel.onValueChange(
                                        name = uiState.newName,
                                        description = it,
                                        backStory = uiState.newBackstory,
                                        imgUri = uiState.imgUri,
                                        visibility = uiState.newVisibility,
                                    )
                                },
                                singleLine = false
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = uiState.newBackstory,
                                textLabel = "BACKSTORY",
                                onValueChange = {
                                    viewModel.onValueChange(
                                        name = uiState.newName,
                                        description = uiState.newDescription,
                                        backStory = it,
                                        imgUri = uiState.imgUri,
                                        visibility = uiState.newVisibility,
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
                                        name = uiState.newName,
                                        description = uiState.newDescription,
                                        backStory = uiState.newBackstory,
                                        imgUri = uiState.imgUri,
                                        visibility = it,
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row {
                                CustomButtonText(
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.showDialog() },
                                    text = "Eliminar personaje",
                                    buttonColor = Color(0xFFF44336)
                                )

                                Spacer(modifier = Modifier.width(20.dp))

                                CustomButtonText(
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.onSaveClick() },
                                    text = "Guardar Cambios",
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
            } else {
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
                                text = "Ver personaje",
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
                                CustomImage(
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .height(100.dp)
                                        .clip(CircleShape),
                                    imgName = uiState.originalImg,
                                    placeHolder = R.drawable.default_avatar,
                                )

                                Spacer(Modifier.width(10.dp))

                                Text(
                                    modifier = Modifier.weight(0.5f),
                                    text = uiState.name,
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            }

                        }

                        item {

                            Spacer(Modifier.width(10.dp))

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = uiState.description,
                                color = Color.White,
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = uiState.backstory,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color(0XFF131313)
    )
}