package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Cuadro de diálogo de confirmación modal.
 *
 * @param onDismissRequest Función llamada cuando se cancela o se cierra el diálogo.
 * @param onConfirmation Función llamada cuando el usuario confirma la acción.
 * @param dialogTitle Título mostrado en el diálogo.
 * @param dialogText Texto descriptivo del propósito del diálogo.
 * @param confirmText Texto del botón de confirmación (por defecto "Aceptar").
 * @param dismissText Texto del botón de cancelación (por defecto "Cancelar").
 */
@Composable
fun DecisionDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmText: String = "Aceptar",
    dismissText: String = "Cancelar"
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(dismissText)
            }
        }
    )
}