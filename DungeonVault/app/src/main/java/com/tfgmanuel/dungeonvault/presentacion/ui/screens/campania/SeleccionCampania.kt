package com.tfgmanuel.dungeonvault.presentacion.ui.screens.campania

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.remote.BASE_URL
import com.tfgmanuel.dungeonvault.presentacion.ui.components.CustomContainer
import com.tfgmanuel.dungeonvault.presentacion.ui.components.DrawerAplicacion
import com.tfgmanuel.dungeonvault.presentacion.ui.components.ListaObjetos
import com.tfgmanuel.dungeonvault.presentacion.ui.components.SheetOption
import com.tfgmanuel.dungeonvault.presentacion.ui.components.TopBarInicio
import com.tfgmanuel.dungeonvault.presentacion.viewmodel.campaniaviewmodel.SeleccionCampaniaViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SeleccionCampania(modifier: Modifier = Modifier, viewModel: SeleccionCampaniaViewModel) {
    val uistate by viewModel.uistate.collectAsState()
    val drawerState =
        androidx.compose.material3.rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isRefreshing = uistate.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.loadCampaigns() }
    )

    ModalNavigationDrawer(
        drawerContent = { DrawerAplicacion() },
        drawerState = drawerState,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopBarInicio(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    })
            },
            content = { paddingValues ->
                Contenido(
                    elementos = uistate.campanias,
                    isRefreshing = isRefreshing,
                    pullRefreshState = pullRefreshState,
                    paddingValues = paddingValues,
                    onClick = { id: Int -> viewModel.onCampaignSelected(id) }
                )
            },
            floatingActionButton = {
                FAB(
                    onClickCreate = { viewModel.onCreateSelected() },
                    onClickInvite = { viewModel.onInviteSelected() }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            containerColor = Color(0XFF131313)
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Contenido(
    elementos: List<Campaign>,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    paddingValues: PaddingValues,
    onClick: (campaignID: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .pullRefresh(pullRefreshState)
    ) {
        ListaObjetos(
            modifier = Modifier
                .fillMaxSize(),
            elementos = elementos,
            paddingValues = paddingValues,
            texto = "Ups, no tienes campañas aún. ¡Crea o agrega una campaña!",
            fontSize = 20.sp
        ) { campania ->
            CustomContainer(
                painter = rememberAsyncImagePainter(
                    model = "${BASE_URL}images/${campania.img_name}",
                    error = painterResource(id = R.drawable.sinimg),
                    placeholder = painterResource(id = R.drawable.sinimg)
                ),
                contentDescription = campania.title,
                titulo = campania.title,
                descripcion = campania.description,
                onClick = { onClick(campania.id) }
            )
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAB(onClickCreate: () -> Unit, onClickInvite: () -> Unit) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showBottomSheet = true },
        shape = CircleShape,
        containerColor = Color(0xFFFDA626),
        contentColor = Color.White
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Color(0xff1a1a1a)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                SheetOption(
                    icono = Icons.Default.Create,
                    titulo = "Crear campaña",
                    subtitulo = "Inicia una nueva aventura como Game Master",
                    onClick = {
                        showBottomSheet = false
                        onClickCreate()
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                SheetOption(
                    icono = Icons.Default.Email,
                    titulo = "Unirse por invitación",
                    subtitulo = "Participa en una campaña ya existente",
                    onClick = {
                        showBottomSheet = false
                        onClickInvite()
                    }
                )
            }
        }
    }
}