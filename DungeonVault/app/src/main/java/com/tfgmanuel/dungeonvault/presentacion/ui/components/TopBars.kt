package com.tfgmanuel.dungeonvault.presentacion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarInicio(
    colorTitulo: Color = Color.White,
    colorContanier: Color = Color(0xFF333333),
    onMenuClick: () -> Unit
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Dungeon Vault",
                color = colorTitulo,
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            shape = CircleShape,
                            color = colorContanier
                        )
                )
                {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = colorTitulo
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarInvitacion(
    colorTitulo: Color = Color.White,
    colorContanier: Color = Color(0xFF1A1A1A),
    colorButton: Color = Color(0xFF333333),
    onBackClick: () -> Unit
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Dungeon Vault",
                color = colorTitulo,
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            shape = CircleShape,
                            color = colorButton
                        )
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = colorTitulo
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = colorContanier)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLogin(
    colorTitulo: Color = Color.White,
    colorContanier: Color = Color(0xFF000000)
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = "Dungeon Vault",
                color = colorTitulo,
                fontSize = 24.sp
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = colorContanier)
    )
}