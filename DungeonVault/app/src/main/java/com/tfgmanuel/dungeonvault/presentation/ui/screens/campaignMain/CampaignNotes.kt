package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.data.model.Note
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomBottomBar
import com.tfgmanuel.dungeonvault.presentation.ui.components.DrawerApplication
import com.tfgmanuel.dungeonvault.presentation.ui.components.FAB
import com.tfgmanuel.dungeonvault.presentation.ui.components.FabSheetOption
import com.tfgmanuel.dungeonvault.presentation.ui.components.ItemList
import com.tfgmanuel.dungeonvault.presentation.ui.components.MainTopBar
import com.tfgmanuel.dungeonvault.presentation.ui.components.TextContainter
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CampaignNotesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CampaignNotes(viewModel: CampaignNotesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState =
        rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isRefreshing = uiState.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.forceUpdate() }
    )

    ModalNavigationDrawer(
        drawerContent = {
            DrawerApplication(
                drawerState = drawerState,
                campaignSelectionEnable = true
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                MainTopBar(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    },
                )
            },
            content = { paddingValues ->
                Content(
                    elements = uiState.notes,
                    userID = uiState.userID,
                    creatorID = uiState.creatorID,
                    isRefreshing = isRefreshing,
                    pullRefreshState = pullRefreshState,
                    paddingValues = paddingValues,
                    onClick = {noteId: Int -> viewModel.onNoteSelected(noteId) }
                )
            },
            floatingActionButton = {
                FAB(
                    options = listOf(
                        FabSheetOption(
                            icon = Icons.Default.Create,
                            title = "Crear nota",
                            subtitle = "Escribe tu propia nota",
                            onClick = { viewModel.onCreateNoteSelected() }
                        ),
                        FabSheetOption(
                            icon = Icons.Default.PlayArrow,
                            title = "Iniciar grabación",
                            subtitle = "Crear transcipción y resumen de la partida",
                            onClick = { viewModel.onCreateTranscription() }
                        )
                    )
                )
            },
            bottomBar = {
                CustomBottomBar(
                    Screen.CampaignNotesScreen.route,
                    { route: String -> viewModel.onItemSelected(route) }
                )
            },
            containerColor = Color(0xFF131313)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(
    elements: List<Note>,
    userID: Int,
    creatorID: Int,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    paddingValues: PaddingValues,
    onClick: (noteId: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .pullRefresh(pullRefreshState)
    ) {
        ItemList(
            modifier = Modifier.fillMaxSize(),
            tList = elements,
            paddingValues = paddingValues,
            text = "No hay notas en esta campaña. ¡Crea una nota o empieza una nueva grabación!",
            fontSize = 20.sp
        ) { note ->
            if (note.visibility || note.user_id == userID || userID == creatorID) {
                TextContainter(
                    modifier = Modifier,
                    onClick = { onClick(note.id) },
                    title = note.title,
                    date = note.creation_date,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.note_icon),
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                )
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}