package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomContainer
import com.tfgmanuel.dungeonvault.presentation.ui.components.DrawerApplication
import com.tfgmanuel.dungeonvault.presentation.ui.components.FAB
import com.tfgmanuel.dungeonvault.presentation.ui.components.FabSheetOption
import com.tfgmanuel.dungeonvault.presentation.ui.components.ItemList
import com.tfgmanuel.dungeonvault.presentation.ui.components.MainTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.CampaignSelectionViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CampaignSelection(modifier: Modifier = Modifier, viewModel: CampaignSelectionViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState =
        androidx.compose.material3.rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isRefreshing = uiState.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.loadCampaigns(forceUpdate = true) }
    )

    ModalNavigationDrawer(
        drawerContent = {
            DrawerApplication(
                drawerState = drawerState
            )
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            modifier = modifier,
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
                    })
            },
            content = { paddingValues ->
                Content(
                    elementos = uiState.campaigns,
                    isRefreshing = isRefreshing,
                    pullRefreshState = pullRefreshState,
                    paddingValues = paddingValues,
                    onClick = { id: Int -> viewModel.onCampaignSelected(id) }
                )
            },
            floatingActionButton = {
                FAB(
                    listOf(
                        FabSheetOption(
                            icon = Icons.Default.Create,
                            title = "Crear Campaña",
                            subtitle = "Inicia una nueva aventura como Game Master",
                            onClick = {  viewModel.onCreateSelected() }
                        ),
                        FabSheetOption(
                            icon = Icons.Default.Email,
                            title = "Unirse por invicación",
                            subtitle = "Participa en una campaña ya existente",
                            onClick = { viewModel.onInviteSelected() }
                        )
                    )
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            containerColor = Color(0XFF131313)
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(
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
        ItemList(
            modifier = Modifier
                .fillMaxSize(),
            tList = elementos,
            paddingValues = paddingValues,
            text = "Ups, no tienes campañas aún. ¡Crea o agrega una campaña!",
            fontSize = 20.sp
        ) { campaign ->
            CustomContainer(
                imgName = campaign.img_name,
                error = R.drawable.sinimg,
                placeholder = R.drawable.sinimg,
                contentDescription = campaign.title,
                title = campaign.title,
                description = campaign.description,
                onClick = { onClick(campaign.id) }
            )
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}