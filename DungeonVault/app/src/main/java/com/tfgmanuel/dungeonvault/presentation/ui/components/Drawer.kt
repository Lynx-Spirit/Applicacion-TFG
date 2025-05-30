package com.tfgmanuel.dungeonvault.presentation.ui.components

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.otherViewModel.MainViewModel

/**
 * Menú hamburguesa de la aplicación.
 */
@Composable
fun DrawerApplication() {
    val viewModel: MainViewModel = hiltViewModel()
    val showDialog by viewModel.showDeleteDialog


    ModalDrawerSheet(
        drawerContainerColor = Color(0xFF1A1A1A),
        drawerContentColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Opciones",
                style = MaterialTheme.typography.headlineLarge
            )

            HorizontalDivider(color = Color(0xFFE69141))

            Text(
                modifier = Modifier.padding(16.dp),
                text = "Ajustes",
                style = MaterialTheme.typography.titleLarge
            )
            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color(0xFF1A1A1A),
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                ),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = null
                    )
                },
                label = { Text("Modificar usuario") },
                selected = false,
                onClick = {
                    viewModel.modifyUser()
                }
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

            Spacer(modifier = Modifier.weight(1f))

            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color(0xFF1A1A1A),
                    unselectedIconColor = Color(0xFFF44336),
                    unselectedTextColor = Color(0xFFF44336)
                ),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                },
                label = { Text("Eliminar cuenta") },
                selected = false,
                onClick = { viewModel.showDialog() }
            )
        }
    }

    if (showDialog) {
        DecisionDialog(
            onDismissRequest = { viewModel.hideDialog() },
            onConfirmation = {
                viewModel.deleteAccount()
                viewModel.hideDialog()
            },
            dialogTitle = "Eliminar cuenta",
            dialogText = "¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer."
        )
    }
}