package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

data class FabSheetOption(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)
