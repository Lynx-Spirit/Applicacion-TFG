package com.tfgmanuel.dungeonvault.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tfgmanuel.dungeonvault.presentation.ui.screens.audio.AudioRecorderScreen
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.CampaignDetails
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.CampaignSelection
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.EnterCampaign
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.NewCampaign
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain.NewNote
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign.UpdateCampaign
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain.ViewNote
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain.CampaignCharacters
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain.CampaignChat
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain.CampaignMainScreen
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain.CampaignNotes
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain.CreateCharacter
import com.tfgmanuel.dungeonvault.presentation.ui.screens.campaignMain.ViewCharacter
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.ChangePassword
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.CreateAccount
import com.tfgmanuel.dungeonvault.presentation.ui.screens.login.Login
import com.tfgmanuel.dungeonvault.presentation.ui.screens.other.UpdateUserInfo
import com.tfgmanuel.dungeonvault.presentation.viewModel.audioViewModel.AudioViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CampaignCharactersViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CampaignChatViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CampaignMainViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CampaignNotesViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.CreateCharacterViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.NewNoteViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.ViewCharacterViewModel
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignMainViewModel.ViewNoteViewModel
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

        composable("${Screen.CampaignMainScreen.route}/{campaignID}") {
            val viewModel: CampaignMainViewModel = hiltViewModel<CampaignMainViewModel>()
            CampaignMainScreen(viewModel = viewModel)
        }

        composable("${Screen.CampaignCharactersScreen.route}/{campaignID}") {
            val viewModel: CampaignCharactersViewModel = hiltViewModel<CampaignCharactersViewModel>()
            CampaignCharacters(viewModel = viewModel)
        }

        composable("${Screen.CampaignNewCharacterScreen.route}/{campaignID}") {
            val viewModel: CreateCharacterViewModel = hiltViewModel<CreateCharacterViewModel>()
            CreateCharacter(viewModel = viewModel)
        }

        composable("${Screen.CampaignViewCharacterScreen.route}/{characterID}") {
            val viewModel: ViewCharacterViewModel = hiltViewModel<ViewCharacterViewModel>()
            ViewCharacter(viewModel = viewModel)
        }

        composable("${Screen.CampaignNotesScreen.route}/{campaignID}") {
            val viewModel: CampaignNotesViewModel = hiltViewModel<CampaignNotesViewModel>()
            CampaignNotes(viewModel = viewModel)
        }

        composable("${Screen.CampaignChatScreen.route}/{campaignID}") {
            val viewModel: CampaignChatViewModel = hiltViewModel<CampaignChatViewModel>()
            CampaignChat(viewModel = viewModel)
        }

        composable("${Screen.CampaignViewNote.route}/{noteID}") {
            val viewModel: ViewNoteViewModel = hiltViewModel<ViewNoteViewModel>()
            ViewNote(viewModel = viewModel)
        }

        composable("${Screen.CampaignNewNote.route}/{campaignID}") {
            val viewModel: NewNoteViewModel = hiltViewModel<NewNoteViewModel>()
            NewNote(viewModel = viewModel)
        }

        composable("${Screen.CampaignTranscription.route}/{campaignID}") {
            val viewModel: AudioViewModel = hiltViewModel<AudioViewModel>()
            AudioRecorderScreen(viewModel = viewModel)
        }
    }
}