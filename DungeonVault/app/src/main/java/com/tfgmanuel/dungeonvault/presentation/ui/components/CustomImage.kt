package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import com.tfgmanuel.dungeonvault.data.remote.BASE_URL

/**
 * Contenedor para imágenes. En caso no tener una imagen, muestra una por defecto.
 *
 * @param modifier Modificador para ajustar el diseño.
 * @param imgName Nombre de la imagen a mostrar.
 * @param placeHolder Imagen por defecto.
 * @param contentScale  Cómo se va a escalar la imagen.
 * @param contentDescription Descripción de la imagen (para accesibilidad).
 */
@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    imgName: String,
    placeHolder: Int,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = ""
) {
    if (imgName != "") {
        val context = LocalContext.current

        val imageLoader = remember {
            ImageLoader.Builder(context)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build()
        }

        val painter = rememberAsyncImagePainter(
            model = "${BASE_URL}images/${imgName}",
            imageLoader = imageLoader,
            error = painterResource(placeHolder)
        )

        Image(
            modifier = modifier,
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(placeHolder),
            contentDescription = contentDescription,
            contentScale = contentScale
        )
    }
}