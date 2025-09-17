package com.ofa.nutralis.ui.util

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ofa.nutralis.navigation.Screen

@Composable
fun BottomBar(
    currentRoute: String,
    navController: NavController,
    bottomScreens: List<Screen>
){
    NavigationBar {
        bottomScreens.forEach { screen ->
            NavigationBarItem(
                icon = { screen.icon?.let { Icon(it, contentDescription = screen.label) } },
                label = { Text(screen.label) },
                selected = currentRoute.startsWith(screen.route.removeSuffix("/{barcode}")),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}