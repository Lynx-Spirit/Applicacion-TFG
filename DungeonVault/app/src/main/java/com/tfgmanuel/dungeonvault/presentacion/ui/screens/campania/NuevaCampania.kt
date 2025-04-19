package com.tfgmanuel.dungeonvault.presentacion.ui.screens.campania

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tfgmanuel.dungeonvault.presentacion.ui.components.TopBarInvitacion
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NuevaCampania(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold (
        modifier = modifier,
        topBar = { TopBarInvitacion(
            onMenuClick = {
                scope.launch {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    } else {
                        drawerState.close()
                    }
                }
            },
            onBackClick = {

            }
        ) },
        content = {  },
        containerColor = Color(0xFF131313)
    )
}



@Preview
@Composable
fun Preview() {
    NuevaCampania(Modifier.fillMaxSize())
}