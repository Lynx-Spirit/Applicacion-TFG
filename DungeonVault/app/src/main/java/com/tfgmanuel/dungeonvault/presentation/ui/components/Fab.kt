package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAB(
    options: List<FabSheetOption>,
    fabIcon: ImageVector = Icons.Default.Add,
    fabColor: Color = Color(0xFFFDA626),
    fabContentColor: Color = Color.White,
    sheetBackgroundColor: Color = Color(0xff1A1A1A)
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showBottomSheet = true },
        shape = CircleShape,
        containerColor = fabColor,
        contentColor = fabContentColor
    ) {
        Icon(fabIcon, contentDescription = null)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = sheetBackgroundColor
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                for (option in options) {
                    SheetOption(
                        icon = option.icon,
                        title = option.title,
                        subtitle = option.subtitle,
                        onClick = {
                            showBottomSheet = false
                            option.onClick()
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@Composable
fun SimpleFAB(
    onClick: () -> Unit,
    fabIcon: ImageVector = Icons.Default.Add,
    fabColor: Color = Color(0xFFFDA626),
    fabContentColor: Color = Color.White,
) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        containerColor = fabColor,
        contentColor = fabContentColor
    ) {
        Icon(fabIcon, contentDescription = null)
    }
}