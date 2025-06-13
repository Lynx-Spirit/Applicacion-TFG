package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.runtime.Composable


data class NavItem(
    val label: String,
    val icon: @Composable () -> Unit,
    val route: String
)
