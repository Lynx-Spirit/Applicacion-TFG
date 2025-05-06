package com.tfgmanuel.dungeonvault.presentation.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonImgText
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomTextField
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.viewmodel.loginviewmodel.CreateAccountViewModel

@Composable
fun CreateAccount(modifier: Modifier = Modifier, viewModel: CreateAccountViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            SecondaryTopBar(
                onBackClick = { viewModel.goBack() },
                containerColor = Color.Black
                )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues)
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondoinicio),
                    contentDescription = "Fondo pantalla inicio",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(450.dp)
                        .align(Alignment.Center)
                        .background(
                            color = Color.Black.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Start),
                            text = "Crear cuenta",
                            color = Color.White,
                            fontSize = 32.sp
                        )

                        HorizontalDivider(color = Color(0xFFE69141))

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomButtonImgText(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .fillMaxHeight(0.105f),
                            onClick = { /*TODO*/ },
                            painter = painterResource(id = R.drawable.googlelogo),
                            contentDescription = "Logo google",
                            text = "Login sesion"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.8f))

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            value = uiState.email,
                            textLabel = "EMAIL",
                            onValueChange = {
                                viewModel.onLoginChanged(
                                    it,
                                    uiState.password,
                                    uiState.confirmPassword
                                )
                            },
                            isPassword = false,
                            isError = uiState.emailResult != null,
                            textError = uiState.emailResult,
                            keyboardType = KeyboardType.Email,
                        )

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            value = uiState.password,
                            textLabel = "CONTRASEÑA",
                            onValueChange = {
                                viewModel.onLoginChanged(
                                    uiState.email,
                                    it,
                                    uiState.confirmPassword
                                )
                            },
                            isPassword = true,
                            isError = uiState.passwordResult != null,
                            textError = uiState.passwordResult,
                            keyboardType = KeyboardType.Password,
                        )

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            value = uiState.confirmPassword,
                            textLabel = "REPETIR CONTRASEÑA",
                            onValueChange = {
                                viewModel.onLoginChanged(
                                    uiState.email,
                                    uiState.password,
                                    it
                                )
                            },
                            isPassword = true,
                            isError = uiState.confirmPasswordResult != null,
                            textError = uiState.confirmPasswordResult,
                            keyboardType = KeyboardType.Password,
                        )

                        CustomButtonText(
                            onClick = { viewModel.registerUser() }, text = "Crear cuenta"
                        )
                    }
                }
            }
        }
    )
}