package com.tfgmanuel.dungeonvault.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.CampaignDetails
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.CampaignSelection
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.EnterCampaign
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.NewCampaign
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.ChangePassword
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.CreateAccount
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.Login
import com.tfgmanuel.dungeonvault.presentation.viewmodel.campaniaviewmodel.CampaignDetailsViewModel
import com.tfgmanuel.dungeonvault.presentation.viewmodel.campaniaviewmodel.CampaignSelectionViewModel
import com.tfgmanuel.dungeonvault.presentation.viewmodel.campaniaviewmodel.EnterCampaignViewModel
import com.tfgmanuel.dungeonvault.presentation.viewmodel.campaniaviewmodel.NewCampaignViewModel
import com.tfgmanuel.dungeonvault.presentation.viewmodel.loginviewmodel.ChangePasswordViewModel
import com.tfgmanuel.dungeonvault.presentation.viewmodel.loginviewmodel.CreateAccountViewModel
import com.tfgmanuel.dungeonvault.presentation.viewmodel.loginviewmodel.LoginViewModel

@Composable
fun NavigationApp(navManager: NavManager, start: String = Screen.Login.route) {
    //Sirve para guardar el historial de navegación
    val navController = rememberNavController()

    //Se encarga de escuchar los eventos de navegación que envían los ViewModels
    //para luego navegar automáticamente entre pantallas.
    LaunchedEffect(navManager) {
        navManager.navigationFlow.collect { command ->
            navController.navigate(command) {
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = start) {
        composable(Screen.Login.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            Login(viewModel = viewModel)
        }

        composable(Screen.CreateAccount.route) {
            val viewModel: CreateAccountViewModel = hiltViewModel<CreateAccountViewModel>()
            CreateAccount(viewModel = viewModel)
        }

        composable(Screen.ChangePassword.route) {
            val viewModel: ChangePasswordViewModel = hiltViewModel<ChangePasswordViewModel>()
            ChangePassword(viewModel = viewModel)
        }

        composable(Screen.SelectCampaign.route) {
            val viewModel: CampaignSelectionViewModel = hiltViewModel<CampaignSelectionViewModel>()
            CampaignSelection(viewModel = viewModel)
        }

        composable(Screen.CreateCampaign.route) {
            val viewModel: NewCampaignViewModel = hiltViewModel<NewCampaignViewModel>()
            NewCampaign(viewModel = viewModel)
        }

        composable(Screen.EnterCampaign.route) {
            val viewModel: EnterCampaignViewModel = hiltViewModel<EnterCampaignViewModel>()
            EnterCampaign(viewModel = viewModel)
        }

        composable("${Screen.CampaignDetails.route}/{campaignID}") {
            val viewModel: CampaignDetailsViewModel = hiltViewModel<CampaignDetailsViewModel>()
            CampaignDetails(viewModel = viewModel)
        }
    }
}