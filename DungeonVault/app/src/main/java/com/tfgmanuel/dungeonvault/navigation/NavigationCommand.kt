package com.tfgmanuel.dungeonvault.navigation

sealed class NavigationCommand {
    data class NavigateTo(
        val route: String,
        val popUpTo: String? = null,
        val inclusive: Boolean = false
    ): NavigationCommand()

    object GoBack : NavigationCommand()
}