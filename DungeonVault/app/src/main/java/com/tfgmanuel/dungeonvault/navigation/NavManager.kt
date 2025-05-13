package com.tfgmanuel.dungeonvault.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavManager @Inject constructor() {

    private val _navigationFlow = MutableSharedFlow<NavigationCommand>()
    val navigationFlow = _navigationFlow.asSharedFlow()

    suspend fun navigate(route: String, popUpTo: String? = null, inclusive: Boolean = false) {
        _navigationFlow.emit(NavigationCommand.NavigateTo(route, popUpTo, inclusive))
    }

    suspend fun goBack() {
        _navigationFlow.emit(NavigationCommand.GoBack)
    }
}