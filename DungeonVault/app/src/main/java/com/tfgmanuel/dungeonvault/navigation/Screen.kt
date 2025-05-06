package com.tfgmanuel.dungeonvault.navigation

sealed class Screen(val route: String) {
    object Login : Screen("Login")
    object CreateAccount : Screen("CreateAccount")
    object ChangePassword : Screen("ChangePassword")
    object SelectCampaign : Screen("SelectCampaign")
    object CampaignDetails : Screen("CampaignDetails")
    object CreateCampaign : Screen("CreateCampaign")
    object EnterCampaign : Screen("EnterCampaign")
}