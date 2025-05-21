package com.tfgmanuel.dungeonvault.presentation.ui.screens.other

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentation.ui.components.ImageSelector
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.otherViewModel.UpdateUserInfoViewModel

@Composable
fun UpdateUserInfo(modifier: Modifier = Modifier, viewModel: UpdateUserInfoViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (
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
                Column (
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = "Modificar usuario",
                        color = Color.White,
                        fontSize = 32.sp
                    )

                    HorizontalDivider(color = Color(0xFFE69141))

                    Spacer(modifier = Modifier.height(15.dp))

                    ImageSelector(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(300.dp),
                        uri = uiState.avatarUri,
                        defaultImage = R.drawable.default_avatar,
                        onImageSelected = { uri ->
                            if (uri != null) {
                                viewModel.onUpdateChanged(
                                    nickname = uiState.nickname,
                                    avatarUri = uri
                                )
                            }
                        },
                        imageName = uiState.originalAvatar
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.nickname,
                        textLabel = "APODO",
                        onValueChange = {
                            viewModel.onUpdateChanged(
                                nickname = it,
                                avatarUri = uiState.avatarUri
                            )
                        },
                        singleLine = true
                    )

                    CustomButtonText(
                        onClick = { viewModel.onSaveClick() },
                        enabled = uiState.isFormValid,
                        text = "Actualizar usuario"
                    )
                }
            }
        },
        containerColor = Color(0xFF131313)
    )
}