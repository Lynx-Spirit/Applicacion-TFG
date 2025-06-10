package com.tfgmanuel.dungeonvault.presentation.viewModel.otherViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.local.dao.UserDAO
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel principal que gestiona las acciones del usuario relacionadas con la sesión,
 * modificación de perfil, y eliminación de cuenta o datos locales.
 *
 * Interactúa con los repositorios y servicios relacionados con autenticación, almacenamiento
 * local de usuario y campañas, y navegación entre pantallas.
 *
 * @property navManager Manejador de navegación.
 * @property tokenManager Manejador de tokens de autenticación.
 * @property campaignDAO DAO para acceso a campañas almacenadas localmente.
 * @property authRepository Repositorio de autenticación (backend).
 * @property userDataStore Almacenamiento local de datos del usuario.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val navManager: NavManager,
    private val tokenManager: TokenManager,
    private val campaignDAO: CampaignDAO,
    private val authRepository: AuthRepository,
    private val userDAO: UserDAO
) : ViewModel() {
    val showDeleteDialog = mutableStateOf(false)

    /**
     * Estado observable para mostrar o esconder el diálogo de confirmación de eliminación de cuenta.
     */
    fun showDialog() {
        showDeleteDialog.value = true
    }

    /**
     * Muestra el diálogo de confirmación para eliminar cuenta.
     */
    fun hideDialog() {
        showDeleteDialog.value = false
    }

    /**
     * Oculta el diálogo de confirmación para eliminar cuenta.
     */
    fun modifyUser() {
        viewModelScope.launch {
            navManager.navigate(Screen.UpdateUser.route)
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     *
     * Limpia los datos locales y navega a la pantalla de login,
     * eliminando el historial de navegación previo.
     */
    fun logOut() {
        viewModelScope.launch {
            deleteAll()
            navManager.navigate(
                route = Screen.Login.route,
                popUpTo = Screen.Login.route,
                inclusive = true
            )
        }
    }

    /**
     * Elimina la cuenta del usuario en el backend y cierra sesión localmente.
     */
    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteUser()
            logOut()
        }
    }

    /**
     * Borra toda la información local del usuario: tokens, campañas locales y preferencias.
     */
    fun deleteAll() {
        viewModelScope.launch {
            tokenManager.clearTokens()
            campaignDAO.deleteAll()
            userDAO.deleteAllUsers()
        }
    }
}