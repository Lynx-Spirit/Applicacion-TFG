package com.tfgmanuel.dungeonvault.presentation.ui.components


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
import com.tfgmanuel.dungeonvault.trimPhrase

@Composable
fun CustomContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = Color(0xFF1A1A1A),
    textColor: Color = Color.White,
    painter: Painter,
    contentDescription: String,
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