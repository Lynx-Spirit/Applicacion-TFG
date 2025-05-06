package com.tfgmanuel.dungeonvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.navigation.NavigationApp
import com.tfgmanuel.dungeonvault.navigation.Screen
import com.tfgmanuel.dungeonvault.presentation.ui.theme.DungeonVaultTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavManager

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

            DungeonVaultTheme (darkTheme = true) {
                if(runBlocking{authRepository.userLoggedIn()}) {
                    NavigationApp(navigationManager, start = Screen.SelectCampaign.route)
                }else {
                    mainViewModel.deleteAll()
                    NavigationApp(navigationManager)
                }
            }
        }
    }
}
