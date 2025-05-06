package com.tfgmanuel.dungeonvault.presentation.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
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
import com.tfgmanuel.dungeonvault.presentation.ui.components.TopBarLogin
import com.tfgmanuel.dungeonvault.presentation.viewmodel.loginviewmodel.LoginViewModel

@Composable
fun Login(modifier: Modifier = Modifier, viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (
        modifier = modifier,
        topBar = {
            TopBarLogin()
        },
        content = {paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
                        .height(360.dp)
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
                            text = "Iniciar sesión",
                            color = Color.White,
                            fontSize = 32.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .fillMaxWidth(0.9f)
                                .background(Color(0xFFFFA726))
                                .align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        CustomButtonImgText(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .fillMaxHeight(0.13f),
                            onClick = { /*TODO*/ },
                            painter = painterResource(id = R.drawable.googlelogo),
                            contentDescription = "Logo google",
                            text = "Login sesion"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .fillMaxWidth(0.8f)
                                .background(Color.White)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            value = uiState.email,
                            textLabel = "EMAIL",
                            onValueChange = { viewModel.onLoginChanged(it, uiState.password) },
                            isError = uiState.error != null,
                            isPassword = false,
                            keyboardType = KeyboardType.Email,
                        )

                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            value = uiState.password,
                            textLabel = "CONTRASEÑA",
                            onValueChange = { viewModel.onLoginChanged(uiState.email, it) },
                            isError = uiState.error != null,
                            textError = uiState.error,
                            isPassword = true,
                            keyboardType = KeyboardType.Password,
                        )

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.5f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    modifier = Modifier.clickable { viewModel.gotOCreateAccount() },
                                    text = "Crear cuenta >",
                                    fontSize = 13.sp,
                                    color = Color.White,
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Text(
                                    modifier = Modifier.clickable { viewModel.goToChangePassword() },
                                    text = "Recuperar contraseña >",
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                            CustomButtonText(
                                onClick = { viewModel.logIn() },
                                text = "Iniciar sesión"
                            )
                        }
                    }
                }
            }
        }
    )
}