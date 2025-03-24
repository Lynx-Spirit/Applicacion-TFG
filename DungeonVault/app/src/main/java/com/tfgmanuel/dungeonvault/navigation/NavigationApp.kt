package com.tfgmanuel.dungeonvault.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tfgmanuel.dungeonvault.presentacion.ui.screens.SeleccionCampania
import com.tfgmanuel.dungeonvault.presentacion.ui.screens.login.CambiarContrasenia
import com.tfgmanuel.dungeonvault.presentacion.ui.screens.login.CrearCuenta
import com.tfgmanuel.dungeonvault.presentacion.ui.screens.login.PagInicio
import com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel.CambiarPassViewModel
import com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel.CrearCuentaViewModel
import com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel.LoginViewModel

@Composable
fun NavigationApp(navManager: NavManager, start: String = Screen.Inicio.route) {
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
        composable(Screen.Inicio.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            PagInicio(modifier = Modifier.fillMaxSize(),viewModel = viewModel)
        }
        composable(Screen.CrearCuenta.route) {
            val viewModel: CrearCuentaViewModel = hiltViewModel<CrearCuentaViewModel>()
            CrearCuenta(modifier = Modifier.fillMaxSize(),viewModel= viewModel)
        }
        composable(Screen.CambiarContrasenia.route) {
            val viewModel: CambiarPassViewModel = hiltViewModel<CambiarPassViewModel>()
            CambiarContrasenia(modifier = Modifier.fillMaxSize(),viewModel= viewModel)
        }
        composable(Screen.SeleccionCampania.route) {
            SeleccionCampania(modifier = Modifier.fillMaxSize())
        }
    }
}