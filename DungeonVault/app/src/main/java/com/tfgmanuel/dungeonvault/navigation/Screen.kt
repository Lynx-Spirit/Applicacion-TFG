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

    /**
     * Ruta de la pantalla para principal de la campaña.
     */
    object CampaignMainScreen: Screen("CampaignMainScreen")

    /**
     * Ruta de la pantala de los personajes que están en la pantalla
     */
    object CampaignCharactersScreen: Screen("CampaignCharacters")

    /**
     * Pantalla para la creación de un nuevo personaje para la campaña.
     */
    object  CampaignNewCharacterScreen: Screen("CampaignNewCharacter")

    /**
     * Pantalla para ver o modificar un personaje.
     */
    object CampaignViewCharacterScreen: Screen("ViewCharacterScreen")

    /**
     * Ruta de la pantalla de notas
     */
    object  CampaignNotesScreen: Screen("CampaignNotes")

    /**
     * Ruta de la pantalla de creación de nota.
     */
    object  CampaignNewNote: Screen("CreateNote")

    /**
     * Ruta de la pantalla de visualizar la nota
     */
    object  CampaignViewNote: Screen("ViewNote")

    /**
     * Ruta de la pantalla de transcripcion
     */
    object  CampaignTranscription: Screen("NewTranscription")

    /**
     * Ruta de la pantalla del chatbot
     */
    object  CampaignChatScreen: Screen("CampaignChat")
}