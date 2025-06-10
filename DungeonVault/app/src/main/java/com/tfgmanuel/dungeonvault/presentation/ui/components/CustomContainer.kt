package com.tfgmanuel.dungeonvault.presentation.ui.components

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.trimPhrase

/**
 * Contenedor personalizado que muestra una imagen, un título y una descripción.
 *
 * @param modifier Modificador para ajustar el diseño.
 * @param onClick Acción a ejecutar al hacer clic.
 * @param shape Forma del contenedor (por defecto esquinas redondeadas).
 * @param containerColor Color de fondo del contenedor.
 * @param textColor Color del texto.
 * @param contentDescription Descripción de la imagen (para accesibilidad).
 * @param imgName Nombre o ruta de la imagen a mostrar.
 * @param error Recurso para imagen de error (por si falla la carga).
 * @param placeholder Recurso para imagen placeholder mientras se carga.
 * @param title Título a mostrar en el contenedor.
 * @param description Descripción o subtítulo.
 */
@Composable
fun CustomContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = Color(0xFF1A1A1A),
    textColor: Color = Color.White,
    contentDescription: String,
    imgName: String,
    error: Int,
    placeholder: Int,
    title: String,
    description: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = containerColor,
                shape = shape
            )
            .clickable(onClick = onClick)
    ) {
        Row {
            CustomImage(
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
                imgName = imgName,
                placeHolder = placeholder,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(5.dp))

            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = trimPhrase(description),
                    color = textColor,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}

/**
 * Contenedor personalizado que muestra una imagen, un título y un icono que se puede ver o no.
 *
 * @param modifier Modificador para ajustar el diseño.
 * @param onClick Acción a ejecutar al hacer clic.
 * @param shape Forma del contenedor (por defecto esquinas redondeadas).
 * @param containerColor Color de fondo del contenedor.
 * @param textColor Color del texto.
 * @param contentDescription Descripción de la imagen (para accesibilidad).
 * @param imgName Nombre o ruta de la imagen a mostrar.
 * @param placeholder Recurso para imagen placeholder mientras se carga.
 * @param title Título a mostrar en el contenedor.
 * @param icon Icono.
 * @param permission Permisos para ver o no el icono.
 *
 */
@Composable
fun SimpleCustomContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = Color.Transparent,
    textColor: Color = Color.White,
    contentDescription: String,
    imgName: String,
    placeholder: Int,
    title: String,
    icon: ImageVector = Icons.Filled.Clear,
    permission: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = containerColor,
                shape = shape
            )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            CustomImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        shape = RoundedCornerShape(100)
                    ),
                imgName = imgName,
                placeHolder = placeholder,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(20.dp))

            Text(
                modifier = Modifier.wrapContentWidth(),
                text = title,
                color = textColor,
                fontSize = 20.sp,
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.weight(1f))

            if (permission) {
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { onClick() },
                    imageVector = icon,
                    tint = Color.Red,
                    contentDescription = "Icono de eliminar"
                )
            }
        }
    }
}

/**
 * Opción de bottom sheet con ícono, título y subtítulo.
 *
 * @param onClick Acción que se ejecuta al seleccionar la opción.
 * @param icon Ícono que se mostrará al principio de la fila.
 * @param title Título principal de la opción.
 * @param subtitle Texto secundario (subtítulo).
 * @param contentColor Color aplicado al ícono, título y subtítulo.
 */
@Composable
fun SheetOption(
    onClick: () -> Unit,
    icon: ImageVector,
    title: String,
    subtitle: String,
    contentColor: Color = Color.White
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
            headlineColor = contentColor,
            supportingColor = contentColor,
            leadingIconColor = contentColor
        ),
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    )
}