package com.tfgmanuel.dungeonvault.presentation.ui.components

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
fun MainTopBar(
    onMenuClick: () -> Unit,
    titleColor: Color = Color.White,
    containerColor: Color = Color(0xFF333333)
) {

    CenterAlignedTopAppBar(
        title = { TopBarTitle(titleColor = titleColor) },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            shape = CircleShape,
                            color = containerColor
                        )
                )
                {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = titleColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(
    titleColor: Color = Color.White,
    containerColor: Color = Color(0xFF1A1A1A),
    buttonColor: Color = Color(0xFF333333),
    onBackClick: () -> Unit
) {

    CenterAlignedTopAppBar(
        title = { TopBarTitle(titleColor = titleColor) },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            shape = CircleShape,
                            color = buttonColor
                        )
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = titleColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = containerColor)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLogin(
    titleColor: Color = Color.White,
    containerColor: Color = Color(0xFF000000)
) {
    CenterAlignedTopAppBar(
        title = { TopBarTitle(titleColor = titleColor) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = containerColor)
    )
}

private const val APP_TITLE = "Dungeon Vault"

@Composable
fun TopBarTitle(titleColor: Color) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = APP_TITLE,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = titleColor
    )
}