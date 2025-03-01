package com.tfgmanuel.dungeonvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tfgmanuel.dungeonvault.presentacion.navigation.NavManager
import com.tfgmanuel.dungeonvault.presentacion.navigation.NavigationApp
import com.tfgmanuel.dungeonvault.presentacion.theme.DungeonVaultTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DungeonVaultTheme {
                NavigationApp(navigationManager)
            }
        }
    }
}
