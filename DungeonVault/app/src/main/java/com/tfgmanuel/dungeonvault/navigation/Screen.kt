package com.tfgmanuel.dungeonvault.navigation

sealed class Screen(val route: String) {
    /**
     * Ruta de la pantalla de inicio se sesión.
     */
    object Login : Screen("Login")

    /**
     * Ruta de la pantalla para la creación de una nueva cuenta.
     */
    object CreateAccount : Screen("CreateAccount")

    /**
     * Ruta de la pantalla para cambiar la contraseña del usuario.
     */
    object ChangePassword : Screen("ChangePassword")

    /**
     * Ruta de la pantalla para actualizar la información del usuario.
     */
    object UpdateUser : Screen("UpdateUser")

    /**
     * Ruta de la pantalla de selección de campaña.
     */
    object SelectCampaign : Screen("SelectCampaign")

    /**
     * Ruta de la pantalla de los detalles de una campaña seleccionada
     */
    object CampaignDetails : Screen("CampaignDetails")

    /**
     * Ruta para crear una nueva campaña.
     */
    object CreateCampaign : Screen("CreateCampaign")

    /**
     * Ruta de la pantalla para unirse o ingresar a una campaña.
     */
    object EnterCampaign : Screen("EnterCampaign")

    /**
     * Ruta de la pantalla para actualizar los detalles de una campaña existente.
     */
    object UpdateCampaign : Screen("UpdateCampaign")
}