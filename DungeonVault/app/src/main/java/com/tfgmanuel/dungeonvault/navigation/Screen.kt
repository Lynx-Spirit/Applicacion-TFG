package com.tfgmanuel.dungeonvault.navigation

sealed class Screen(val route: String) {
    object Inicio: Screen("Inicio")
    object CrearCuenta: Screen("CrearCuenta")
    object CambiarContrasenia: Screen("CambiarContrasenia")
    object SeleccionCampania: Screen("SeleccionCampania")
}