package com.tfgmanuel.dungeonvault.presentacion.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> ListaObjetos(
    modifier: Modifier = Modifier,
    elementos: List<T>,
    texto: String,
    fontSize: TextUnit = 24.sp,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    itemContent: @Composable (T) -> Unit
) {
    if (elementos.isEmpty()) {
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = texto,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                fontSize = fontSize
            )
        }
    } else {
        LazyColumn (
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = elementos) { elemento ->
                itemContent(elemento)
            }
        }
    }
}