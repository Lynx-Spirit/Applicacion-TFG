package com.tfgmanuel.dungeonvault.presentacion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButtonText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    shape : Shape = RoundedCornerShape(8.dp),
    colorButton: Color = Color(0xFFFFA726),
    disableColorButton: Color = Color(0xFFFFD699),
    colorText: Color = Color.White,
    disabledcolorText: Color = Color(0xFF757575 )
) {
    Button (
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = ButtonColors (
            containerColor = colorButton,
            contentColor = colorText,
            disabledContainerColor = disableColorButton,
            disabledContentColor = disabledcolorText)
    ) {
       Text(text = text)
    }
}

@Composable
fun CustomButtonImg(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentDescription: String,
    painter: Painter,
    shape : Shape = RoundedCornerShape(8.dp),
    colorButton: Color = Color(0xFF1C1C1F),
) {
    Button (
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = ButtonColors (
            containerColor = colorButton,
            contentColor = colorButton,
            disabledContentColor = colorButton,
            disabledContainerColor =  colorButton)
    ) {
            Image (
                modifier = Modifier.fillMaxHeight(0.95f),
                alignment = Alignment.Center,
                painter = painter,
                contentDescription = contentDescription
            )
    }
}

@Composable
fun CustomButtonImgText (
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(8.dp),
    buttonColor: Color = Color(0xFF1C1C1F),
    textColor: Color = Color.White,
    painter: Painter,
    contentDescription: String,
    text: String
){
    Button (
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = ButtonColors (
            containerColor = buttonColor,
            contentColor = buttonColor,
            disabledContentColor = buttonColor,
            disabledContainerColor =  buttonColor)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Image(
                modifier = Modifier.fillMaxHeight(0.7f),
                alignment = Alignment.Center,
                painter = painter,
                contentDescription = contentDescription
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = text,
                color = textColor,
                textAlign = TextAlign.Center,
                fontSize = 12.sp)
        }
    }
}
