package com.tfgmanuel.dungeonvault.presentacion.ui.screens.campania

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.remote.BASE_URL
import com.tfgmanuel.dungeonvault.presentacion.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentacion.ui.components.CustomIconButon
import com.tfgmanuel.dungeonvault.presentacion.ui.components.TopBarInvitacion
import com.tfgmanuel.dungeonvault.presentacion.viewmodel.campaniaviewmodel.DetallesCampaniaViewModel

@Composable
fun DetallesCampania(modifier: Modifier = Modifier, viewModel: DetallesCampaniaViewModel) {
    val campania = viewModel.campaign.value
    if (campania != null) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopBarInvitacion(
                    onBackClick = {
                        viewModel.goBack()
                    }
                )
            },
            content = { paddingValues ->
                ImagenFondo(paddingValues = paddingValues, imgName = campania.img_name)
                Contenido(
                    paddingValues = paddingValues,
                    campania = campania,
                    permission = viewModel.checkPermission()
                )
            }
        )
    }
}

@Composable
fun ImagenFondo(paddingValues: PaddingValues, imgName: String) {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        painter = rememberAsyncImagePainter(
            model = "${BASE_URL}images/${imgName}",
            error = painterResource(id = R.drawable.sinimg),
            placeholder = painterResource(id = R.drawable.sinimg)
        ),
        contentDescription = "Imagen de fondo de la camapaña",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun Contenido(paddingValues: PaddingValues, campania: Campaign, permission: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = campania.title,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
            )

            BotonesCampania(
                permission = permission,
                onComenzarClick = {},
                onEditarClick = {},
                onEliminarClick = {}
            )

            CodigoInvitacion(campania.invite_code)

            Spacer(modifier = Modifier.height(10.dp))

            InformacionCampania(campania.description)
        }
    }
}

@Composable
fun BotonesCampania(
    permission: Boolean,
    onComenzarClick: () -> Unit,
    onEditarClick: () -> Unit,
    onEliminarClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .padding(10.dp)
            .background(
                color = Color(0xFF131313),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column (
            modifier = Modifier.padding(10.dp)
        ){
            CustomButtonText(
                modifier = Modifier.fillMaxWidth(),
                text = "COMENZAR",
                fontSize = 18.sp,
                onClick = { onComenzarClick() }
            )

            Spacer(modifier = Modifier.height(5.dp))
            if (permission) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomIconButon(
                        modifier = Modifier.weight(1f),
                        color = Color.Transparent,
                        icono = Icons.Default.Edit,
                        text = "EDITAR",
                        onClick = { onEditarClick() }
                    )

                    VerticalDivider(modifier = Modifier.height(50.dp))

                    CustomIconButon(
                        modifier = Modifier.weight(1f),
                        color = Color.Transparent,
                        icono = Icons.Default.Delete,
                        colorTexto = Color(0xFFF44336),
                        text = "ELIMINAR",
                        onClick = { onEliminarClick() }
                    )
                }
            } else {
                CustomIconButon(
                    color = Color.Transparent,
                    icono = Icons.AutoMirrored.Filled.ExitToApp,
                    colorTexto = Color(0xFFF44336),
                    text = "ABANDONAR CAMPAÑA",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun InformacionCampania(descripcion: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .background(
                color = Color(0xFF131313),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column (
            modifier = Modifier.padding(10.dp)
        ){
            Text(
                text = "Descripcion",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = descripcion,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun CodigoInvitacion(inviteCode: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .background(
                color = Color(0xFF131313),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Código de invitación",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = inviteCode,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}