package com.tfgmanuel.dungeonvault.navigation

sealed class NavigationCommand {
    /**
     * Comando para navegar a una ruta específica dentro de la aplicación.
     *
     * @param route Ruta de destino a la que se desea navegar.
     * @param popUpTo (Opcional) Ruta a la que se desea hacer pop en la pila de navegación. Si se proporciona,
     *                elimina todas las rutas desde la actual hasta esta ruta (inclusive si [inclusive] es true).
     * @param inclusive Indica si la ruta especificada en [popUpTo] debe ser incluida en el pop.
     */
    data class NavigateTo(
        val route: String,
        val popUpTo: String? = null,
        val inclusive: Boolean = false
    ): NavigationCommand()

    /**
     * Comando para retroceder en la pila de navegación.
     */
    object GoBack : NavigationCommand()
}