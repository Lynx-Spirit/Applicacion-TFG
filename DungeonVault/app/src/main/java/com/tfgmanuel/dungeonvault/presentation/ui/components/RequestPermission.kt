package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission (
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable () -> Unit
) {
    val permissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    when {
        permissionState.status.isGranted -> {
            onPermissionGranted()
        }

        permissionState.status.shouldShowRationale -> {
            Text("Es necesario aceptar el permiso de audio para poder usar la aplicación")
        }

        else -> {
            onPermissionDenied()
        }
    }
}