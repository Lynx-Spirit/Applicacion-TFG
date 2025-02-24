package com.tfgmanuel.dungeonvault.presentacion.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.presentacion.ui.components.CustomButtonImgText
import com.tfgmanuel.dungeonvault.presentacion.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentacion.ui.components.CustomTextField

@Preview
@Composable
fun CambiarContrasenia(modifier: Modifier = Modifier) {
    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Image (
            painter = painterResource(id = R.drawable.fondoinicio),
            contentDescription = "Fondo pantalla inicio",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .clickable { /**/ }
                .padding(horizontal = 10.dp),
            text = "<",
            color = Color.White,
            fontSize = 40.sp
        )

        Text (
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(10.dp),
            text = "Dungeon Vault",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Box (
            modifier = Modifier.fillMaxWidth(0.85f)
                .height(280.dp)
                .align(Alignment.Center)
                .background (
                    color = Color.Black.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    modifier = Modifier.align(Alignment.Start),
                    text = "Cambiar clave",
                    color = Color.White,
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box (
                    modifier = Modifier.height(2.dp)
                        .fillMaxWidth(0.9f)
                        .background(Color(0xFFFFA726))
                        .align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomButtonImgText (
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.19f),
                    onClick = { /*TODO*/},
                    painter = painterResource(id = R.drawable.googlelogo),
                    contentDescription = "Logo google",
                    text = "Inicio sesion"
                )

                Spacer(modifier = Modifier.height(15.dp))

                Box (
                    modifier = Modifier.height(2.dp)
                        .fillMaxWidth(0.8f)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomTextField (
                    value = "EMAIL",
                    textLabel = "EMAIL",
                    onValueChange = {},
                    isPassword = false,
                    keyboardType = KeyboardType.Email,
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomButtonText (
                    onClick = { },
                    text = "Cambiar clave"
                )
            }
        }
    }
}