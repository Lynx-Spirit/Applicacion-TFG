package com.tfgmanuel.dungeonvault.presentacion.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    containterColor: Color = Color(0xFF1A1A1A),
    textColor: Color = Color.White,
    painter: Painter,
    contentDescription: String,
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = containterColor,
                shape = shape
            )
            .clickable(onClick = onClick)
    ) {
        Row {
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 0.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 0.dp,
                        )
                    ),
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(5.dp))

            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = titulo,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = descripcion,
                    color = textColor,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}

@Composable
fun SheetOption(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    contentColor: Color = Color.White,
    onClick: () -> Unit,
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
            headlineColor = contentColor,
            supportingColor = contentColor,
            leadingIconColor = contentColor
        ),
        headlineContent = { Text(titulo) },
        supportingContent = { Text(subtitulo) },
        leadingContent = {
            Icon(icono, contentDescription = null)
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}