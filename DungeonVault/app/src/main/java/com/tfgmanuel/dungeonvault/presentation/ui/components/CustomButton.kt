package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CustomButtonText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    buttonColor: Color = Color(0xFFFFA726),
    disabledButtonColor: Color = Color(0xFFFFD699),
    textColor: Color = Color.White,
    disabledTextColor: Color = Color(0xFF757575),
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = ButtonColors(
            containerColor = buttonColor,
            contentColor = textColor,
            disabledContainerColor = disabledButtonColor,
            disabledContentColor = disabledTextColor
        )
    ) {
        Text(
            text = text,
            fontSize = fontSize
        )
    }
}

@Composable
fun CustomButtonImg(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    buttonColor: Color = Color(0xFF1C1C1F),
    contentDescription: String,
    painter: Painter
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = ButtonColors(
            containerColor = buttonColor,
            contentColor = buttonColor,
            disabledContentColor = buttonColor,
            disabledContainerColor = buttonColor
        )
    ) {
        Image(
            modifier = Modifier.fillMaxHeight(0.95f),
            alignment = Alignment.Center,
            painter = painter,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun CustomButtonImgText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    buttonColor: Color = Color(0xFF1C1C1F),
    textColor: Color = Color.White,
    fontSize: TextUnit = TextUnit.Unspecified,
    painter: Painter,
    contentDescription: String,
    text: String
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = ButtonColors(
            containerColor = buttonColor,
            contentColor = buttonColor,
            disabledContentColor = buttonColor,
            disabledContainerColor = buttonColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                fontSize = fontSize
            )
        }
    }
}

@Composable
fun CustomIconButon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    buttonColor: Color = Color(0xFFFDA626),
    disabledButtonColor: Color = Color(0xFFFFD699),
    textColor: Color = Color.White,
    disabledTextColor: Color = Color(0xFF757575),
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    icon: ImageVector
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = ButtonColors(
            containerColor = buttonColor,
            contentColor = textColor,
            disabledContainerColor = disabledButtonColor,
            disabledContentColor = disabledTextColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                fontSize = fontSize
            )
        }
    }
}

@Composable
fun CustomActionButton(
    onClick: () -> Unit,
    shape: Shape = CircleShape,
    buttonColor: Color = Color(0xFFFDA626),
    textColor: Color = Color.White,
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = buttonColor,
        shape = shape
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize
        )
    }
}
