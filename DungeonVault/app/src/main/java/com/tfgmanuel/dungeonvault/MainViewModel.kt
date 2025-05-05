package com.tfgmanuel.dungeonvault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.TokenManager
import com.tfgmanuel.dungeonvault.data.local.dao.CampaignDAO
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var navManager: NavManager,
    private var tokenManager: TokenManager,
    private var campaignDAO: CampaignDAO,
    private var authRepository: AuthRepository
): ViewModel(){

    fun logOut() {
        viewModelScope.launch {
            deleteAll()
            navManager.navigate(Screen.Inicio.route)
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
        }
    }
}