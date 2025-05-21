package com.tfgmanuel.dungeonvault.presentation.viewModel.otherViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.UserDataStore
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navManager: NavManager,
    private val tokenManager: TokenManager,
    private val campaignDAO: CampaignDAO,
    private val authRepository: AuthRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {
    val showDeleteDialog = mutableStateOf(false)

    fun showDialog() {
        showDeleteDialog.value = true
    }

    fun hideDialog() {
        showDeleteDialog.value = false
    }

    fun modifyUser() {
        viewModelScope.launch {
            navManager.navigate(Screen.UpdateUser.route)
        }
    }

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

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteUser()
            logOut()
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            tokenManager.clearTokens()
            campaignDAO.deleteAll()
            userDataStore.deleteInformation()
        }
    }
}