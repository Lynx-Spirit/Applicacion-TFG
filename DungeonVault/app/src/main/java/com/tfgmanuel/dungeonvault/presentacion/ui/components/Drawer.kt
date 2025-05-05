package com.tfgmanuel.dungeonvault.presentacion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tfgmanuel.dungeonvault.MainViewModel

@Composable
fun DrawerAplicacion() {
    val viewModel: MainViewModel = hiltViewModel()
    val openAlertDialog = remember { mutableStateOf(false) }

    ModalDrawerSheet(
        drawerContainerColor = Color(0xFF1A1A1A),
        drawerContentColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                "Opciones",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineLarge
            )

            HorizontalDivider(color = Color(0xFFE69141))

            Text(
                "Ajustes",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color(0xFF1A1A1A),
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                ),
                icon = { Icon(imageVector = Icons.Default.Create, contentDescription = null) },
                label = { Text("Modificar usuario") },
                selected = false,
                onClick = { /* Handle click */ }
            )

            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color(0xFF1A1A1A),
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                ),
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null
                    )
                },
                label = { Text("Logout") },
                selected = false,
                onClick = {
                    viewModel.logOut()
                }
            )

            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color(0xFF1A1A1A),
                    unselectedIconColor = Color(0xFFF44336),
                    unselectedTextColor = Color(0xFFF44336)
                ),
                icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = null) },
                label = { Text("Eliminar cuenta") },
                selected = false,
                onClick = { openAlertDialog.value = true }
            )
        }
    }

    if(openAlertDialog.value) {
        DecisionDialog(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                viewModel.deleteAccount()
                openAlertDialog.value = false
            },
            dialogTitle = "Eliminar cuenta",
            dialogText = "¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer."
        )
    }
}