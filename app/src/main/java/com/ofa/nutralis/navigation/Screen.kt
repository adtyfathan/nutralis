package com.ofa.nutralis.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Scan : Screen("product_scan", "Scan", Icons.Default.AddCircle)
    object Input : Screen("product_input", "Search")
    object Result : Screen("product_result/{barcode}", "Result"){
        fun createRoute(barcode: String) = "product_result/$barcode"
    }
    object Scanned : Screen("scanned_product", "Scanned", Icons.Default.Menu)
    object Profile : Screen("profile", "Profile")
    object Compare : Screen("compare", "Compare", Icons.Default.Info)
    object CompareResult : Screen("compare_result/{barcode1}/{barcode2}", "Compare Result"){
        fun createRoute(barcode1: String, barcode2: String) = "compare_result/$barcode1/$barcode2"

    }
}