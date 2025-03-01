package com.tfgmanuel.dungeonvault.presentacion.navigation

sealed class Screen(val route: String) {
    object Inicio: Screen("Inicio")
    object CrearCuenta: Screen("CrearCuenta")
    object CambiarContrasenia: Screen("CambiarContrasenia")
}