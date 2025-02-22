package com.tfgmanuel.dungeonvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.tfgmanuel.dungeonvault.ui.screens.PagInicio
import com.tfgmanuel.dungeonvault.ui.theme.DungeonVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DungeonVaultTheme {
                PagInicio(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
