package com.example.nutralis.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Scan : Screen("product_scan", "Scan", Icons.Default.AddCircle)
    object Input : Screen("product_input", "Search", Icons.Default.Search)
    object Result : Screen("product_result/{barcode}", "Result"){
        fun createRoute(barcode: String) = "product_result/$barcode"
    }
}