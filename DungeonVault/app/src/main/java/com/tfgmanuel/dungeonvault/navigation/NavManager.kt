package com.tfgmanuel.dungeonvault.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavManager @Inject constructor() {

    private val _navigationFlow = MutableSharedFlow<NavigationCommand>()

    /**
     * Flujo de solo lectura que expone los comandos de navegación.
     * Puede ser observado por la UI para reaccionar ante los eventos de navegación emitidos.
     */
    val navigationFlow = _navigationFlow.asSharedFlow()

    /**
     * Emite un comando de navegación hacia la ruta especificada.
     *
     * @param route Ruta de destino a la que se desea navegar.
     * @param popUpTo (Opcional) Ruta a la que se desea hacer pop en la pila de navegación.
     * @param inclusive Indica si la ruta especificada en [popUpTo] debe ser incluida en el pop.
     */
    suspend fun navigate(route: String, popUpTo: String? = null, inclusive: Boolean = false) {
        _navigationFlow.emit(NavigationCommand.NavigateTo(route, popUpTo, inclusive))
    }

    /**
     * Emite un comando de retroceder en la pila de navegación.
     */
    suspend fun goBack() {
        _navigationFlow.emit(NavigationCommand.GoBack)
    }
}