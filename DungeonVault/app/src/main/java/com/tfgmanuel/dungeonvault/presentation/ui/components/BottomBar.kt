package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.navigation.Screen


/**
 *
 */
@Composable
fun CustomBottomBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    selectedColor: Color = Color(0xFFDC8C40),
    unselectedColor: Color = Color.White,
    disabledColor: Color = Color.Gray
) {
    val iconSize = 24.dp

    val items = listOf(
        NavItem(
            label = "Campaña",
            icon = {
                Icon(
                    painter = painterResource(R.drawable.flag),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            },
            route = Screen.CampaignMainScreen.route
        ),
        NavItem(
            label = "Personajes",
            icon = {
                Icon(
                    painter = painterResource(R.drawable.people),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            },
            route = Screen.CampaignCharactersScreen.route
        ),
        NavItem(
            label = "Notas",
            icon = {
                Icon(
                    painter = painterResource(R.drawable.notes),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            },
            route = Screen.CampaignNotesScreen.route
        ),
        NavItem(
            label = "Chat",
            icon = {
                Icon(
                    painter = painterResource(R.drawable.chat),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            },
            route = Screen.CampaignChatScreen.route
        )
    )

    NavigationBar(
        containerColor = Color(0xFF1B1B1B)
    ) {
        items.forEach { navItem ->
            val selected = selectedRoute == navItem.route

            NavigationBarItem(
                selected = selected,
                onClick = { onItemSelected(navItem.route) },
                icon = navItem.icon,
                label = { Text(navItem.label) },
                colors = NavigationBarItemColors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    disabledIconColor = disabledColor,
                    disabledTextColor = disabledColor
                )
            )
        }
    }
}