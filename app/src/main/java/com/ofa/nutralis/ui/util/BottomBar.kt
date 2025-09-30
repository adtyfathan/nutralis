package com.ofa.nutralis.ui.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ofa.nutralis.R
import com.ofa.nutralis.navigation.Screen

@Composable
fun BottomBar(
    currentRoute: String,
    navController: NavController,
    bottomScreens: List<Screen>
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        // Home
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home), // your image here
                    contentDescription = bottomScreens[0].label,
                    tint = if (currentRoute.startsWith(bottomScreens[0].route)) Color(0xFF4CAF50) else Color.Gray
                )
            },
            label = { Text(bottomScreens[0].label) },
            selected = currentRoute.startsWith(bottomScreens[0].route),
            onClick = { navigate(navController, bottomScreens[0].route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50), // green
                selectedTextColor = Color(0xFF4CAF50),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent // removes default highlight
            )
        )
        Spacer(Modifier.weight(1f))

        // History
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.history), // your image here
                    contentDescription = bottomScreens[2].label,
                    tint = if (currentRoute.startsWith(bottomScreens[2].route)) Color(0xFF4CAF50) else Color.Gray
                )
            },
            label = { Text(bottomScreens[2].label) },
            selected = currentRoute.startsWith(bottomScreens[2].route),
            onClick = { navigate(navController, bottomScreens[2].route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50), // green
                selectedTextColor = Color(0xFF4CAF50),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent // removes default highlight
            )
        )
    }
}

private fun navigate(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
        launchSingleTop = true
        restoreState = true
    }
}