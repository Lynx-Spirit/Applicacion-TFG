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

private const val APP_TITLE = "Dungeon Vault"

/**
 * Barra superior principal que muestra el icono de menú en la navegación y el título de la aplicación.
 *
 * @param onMenuClick Función llamada cuando el usuario hace clic en el icono del menú.
 * @param titleColor Color del texto del título de la barra superior (por defecto blanco).
 * @param containerColor Color del fondo del contenedor de la barra superior (por defecto un gris oscuro).
 */
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

/**
 * Barra superior secundaria, generalmente usada con un icono de "volver".
 *
 * @param titleColor Color del texto del título de la barra superior.
 * @param containerColor Color del fondo del contenedor de la barra superior.
 * @param buttonColor Color del fondo del botón de navegación.
 * @param onBackClick Función llamada cuando el usuario hace clic en el botón de "volver".
 */
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

/**
 * Barra superior para la pantalla de inicio de sesión, que solo muestra el título.
 *
 * @param titleColor Color del texto del título de la barra superior.
 * @param containerColor Color del fondo del contenedor de la barra superior.
 */
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

/**
 * Título que se muestra en las barras superiores.
 *
 * @param titleColor Color del texto del título de la barra superior.
 */
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