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
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.UpdateCampaign
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.ChangePassword
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.CreateAccount
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.Login
import com.tfgmanuel.dungeonvault.presentation.ui.screens.other.UpdateUserInfo
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.CampaignDetailsViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.CampaignSelectionViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.EnterCampaignViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.NewCampaignViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.UpdateCampaignViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.loginViewModel.ChangePasswordViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.loginViewModel.CreateAccountViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.loginViewModel.LoginViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.otherViewModel.UpdateUserInfoViewModel

/**
 * Se encarga de la navegación de la aplicación.
 *
 * @param navManager Gestor de la navegación
 * @param start Primera pantalla en la que la aplicacóin iniciará.
 */
@Composable
fun NavigationApp(navManager: NavManager, start: String = Screen.Login.route) {
    // Sirve para guardar el historial de navegación
    val navController = rememberNavController()

    // Se encarga de escuchar los eventos de navegación que envían los ViewModels
    // para luego navegar automáticamente entre pantallas.
    LaunchedEffect(navManager) {
        navManager.navigationFlow.collect { command ->
            when (command) {
                is NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        command.popUpTo?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) {
                                inclusive = command.inclusive
                            }
                        }
                        launchSingleTop = true
                    }
                }
                is NavigationCommand.GoBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    // Direcciones a las que la aplicación puede navegar.
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

        composable(Screen.UpdateUser.route) {
            val viewModel: UpdateUserInfoViewModel = hiltViewModel<UpdateUserInfoViewModel>()
            UpdateUserInfo(viewModel = viewModel)
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

        composable("${Screen.UpdateCampaign.route}/{campaignID}") {
            val viewModel: UpdateCampaignViewModel = hiltViewModel<UpdateCampaignViewModel>()
            UpdateCampaign(viewModel = viewModel)
        }
    }
}