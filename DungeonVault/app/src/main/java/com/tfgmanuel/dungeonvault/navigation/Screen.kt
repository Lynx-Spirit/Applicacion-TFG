package com.tfgmanuel.dungeonvault.navigation

sealed class Screen(val route: String) {
    object Inicio: Screen("Inicio")
    object CrearCuenta: Screen("CrearCuenta")
    object CambiarContrasenia: Screen("CambiarContrasenia")
    object SeleccionCampania: Screen("SeleccionCampania")
    object DetalleCampania: Screen("DetalleCampania")
    object CrearCampania: Screen("NuevaCampania")
    object EntrarCampania: Screen("EntrarCampania")
}