package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
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


@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    imgName: String,
    error: Int,
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
            placeholder = painterResource(placeHolder),
            error = painterResource(error)
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