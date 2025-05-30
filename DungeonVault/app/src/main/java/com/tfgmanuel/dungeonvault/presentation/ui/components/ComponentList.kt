package com.tfgmanuel.dungeonvault.presentation.ui.components

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

/**
 * Componente reutilizable que muestra una lista de elementos usando un [LazyColumn].
 * Si la lista está vacía, muestra un mensaje personalizado en su lugar.
 *
 * @param modifier Modificador para aplicar al contenedor principal.
 * @param paddingValues Relleno externo aplicado tanto en la vista vacía como en la lista.
 * @param tList Lista de elementos a renderizar.
 * @param text Texto que se muestra cuando la lista está vacía.
 * @param fontSize Tamaño de fuente para el texto de lista vacía. Por defecto es 24sp.
 * @param itemContent Función que define cómo se debe renderizar cada elemento de la lista.
 */
@Composable
fun <T> ItemList(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    tList: List<T>,
    text: String,
    fontSize: TextUnit = 24.sp,
    itemContent: @Composable (T) -> Unit
) {
    if (tList.isEmpty()) {
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontSize = fontSize
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = tList) { element ->
                itemContent(element)
            }
        }
    }
}