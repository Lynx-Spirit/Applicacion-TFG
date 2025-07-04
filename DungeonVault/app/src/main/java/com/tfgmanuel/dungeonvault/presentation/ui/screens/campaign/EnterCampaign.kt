package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.EnterCampaignViewModel

@Composable
fun EnterCampaign(modifier : Modifier = Modifier, viewModel: EnterCampaignViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (
        modifier = modifier,
        topBar = {
            SecondaryTopBar (
                onBackClick = {
                    viewModel.goBack()
                }
            )
        },
        content = {paddingValues ->
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
                        text = "Entrar a partida",
                        color = Color.White,
                        fontSize = 32.sp
                    )

                    HorizontalDivider(color = Color(0xFFE69141))

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.inviteCode,
                        textLabel = "CODIGO INVITACIÓN",
                        textError = uiState.error,
                        isError = uiState.error != null,
                        onValueChange = {
                            viewModel.onInviteChange(it)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomButtonText(
                        onClick = { viewModel.onInviteSelected() },
                        text = "Entrar a partida",
                        enabled = uiState.isInviteValid
                    )
                }
            }
        },
        containerColor = Color(0xFF131313)
    )
}