package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
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
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomBottomBar
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomContainer
import com.tfgmanuel.dungeonvault.presentation.ui.components.DrawerApplication
import com.tfgmanuel.dungeonvault.presentation.ui.components.ItemList
import com.tfgmanuel.dungeonvault.presentation.ui.components.MainTopBar
import com.tfgmanuel.dungeonvault.presentation.ui.components.SimpleFAB
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CampaignCharactersViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CampaignCharacters(viewModel: CampaignCharactersViewModel) {
    val drawerState =
        androidx.compose.material3.rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .pullRefresh(pullRefreshState)
                ) {
                    ItemList(
                        modifier = Modifier
                            .fillMaxSize(),
                        tList = uiState.characters,
                        paddingValues = paddingValues,
                        text = "Ups, no tienes ningún personaje aún. ¡Cree un nuevo personaje!",
                        fontSize = 20.sp
                    ) { character ->
                        if (character.user_id == uiState.userID || uiState.creatorID == uiState.userID) {
                            CustomContainer(
                                imgName = character.img_name,
                                error = R.drawable.random_img,
                                placeholder = R.drawable.random_img,
                                contentDescription = character.name,
                                title = character.name,
                                description = character.description,
                                onClick = { viewModel.onCharacterSelected(character.id) }
                            )
                        }
                    }
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            },
            floatingActionButton = {
                SimpleFAB(onClick = { viewModel.onCreateSelected() })
            },
            bottomBar = {
                CustomBottomBar(
                    Screen.CampaignCharactersScreen.route,
                    { route: String -> viewModel.onItemSelected(route) }
                )
            },
            containerColor = Color(0xFF131313)
        )
    }
}