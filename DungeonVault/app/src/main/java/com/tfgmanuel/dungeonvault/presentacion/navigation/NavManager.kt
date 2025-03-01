package com.tfgmanuel.dungeonvault.presentacion.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavManager @Inject constructor() {

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow = _navigationFlow.asSharedFlow()

    suspend fun navigate(route: String) {
        _navigationFlow.emit(route)
    }
}