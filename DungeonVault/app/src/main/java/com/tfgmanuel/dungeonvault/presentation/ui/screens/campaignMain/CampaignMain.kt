package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomBottomBar
import com.tfgmanuel.dungeonvault.presentation.ui.components.DrawerApplication
import com.tfgmanuel.dungeonvault.presentation.ui.components.MainTopBar
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CampaignMainViewModel
import kotlinx.coroutines.launch

@Composable
fun CampaignMainScreen(viewModel: CampaignMainViewModel) {
    val drawerState =
        androidx.compose.material3.rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                Text(
                    modifier = Modifier.padding(paddingValues),
                    text = "Placeholder",
                    color = Color.White
                )
            },
            bottomBar = {
                CustomBottomBar(
                    Screen.CampaignMainScreen.route,
                    { route: String -> viewModel.onItemSelected(route) }
                )
            },
            containerColor = Color(0xFF131313)
        )
    }
}