package com.tfgmanuel.dungeonvault.presentation.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.data.remote.BASE_URL

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    uri: Uri?,
    imageName: String = "",
    defaultImage:Int,
    onImageSelected: (Uri?) -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { newUri ->
            if (newUri != null) {
                onImageSelected(newUri)
            }
        }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (uri != Uri.EMPTY) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(
                    model = uri
                ),
                contentDescription = "Imagen seleccionada",
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(
                    model = "${BASE_URL}images/${imageName}",
                    error = painterResource(id = defaultImage),
                    placeholder = painterResource(id = defaultImage)
                ),
                contentDescription = "Imagen base",
                contentScale = ContentScale.Crop
            )
        }

        IconButton(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                .size(48.dp),
            onClick = {
                launcher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Cambiar imagen",
                tint = Color.White
            )
        }
    }
}