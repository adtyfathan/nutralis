package com.example.nutralis.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutralis.ui.auth.AuthViewModel
import com.example.nutralis.ui.home.HomeScreen
import com.example.nutralis.ui.product.ProductInputScreen
import com.example.nutralis.ui.product.ProductResultScreen
import com.example.nutralis.ui.product.ProductScanScreen
import com.example.nutralis.ui.product.ScannedProductScreen
import com.example.nutralis.ui.profile.ProfileScreen
import com.example.nutralis.ui.util.BottomBar
import com.example.nutralis.ui.util.MainTopbar
import com.example.nutralis.ui.util.SearchTopbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val bottomScreens = listOf(Screen.Home, Screen.Scan, Screen.Scanned)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()

    Scaffold (
        topBar = {
            when {
                currentRoute == Screen.Input.route -> {
                    SearchTopbar(
                        onBack = { navController.popBackStack() },
                        onSearch = { query ->
                            navController.navigate(Screen.Result.createRoute(query))
                        }
                    )
                }
                else -> {
                    MainTopbar(
                        currentRoute,
                        navController,
                        onProfileClick = { navController.navigate(Screen.Profile.route) },
                        authViewModel = authViewModel
                    )
                }
            }
        },
        bottomBar = {
            BottomBar(currentRoute, navController, bottomScreens)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Scan.route) { ProductScanScreen { barcode -> navController.navigate(Screen.Result.createRoute(barcode)) } }
            composable(Screen.Input.route) { ProductInputScreen( onHistoryClick = { barcode -> navController.navigate(Screen.Result.createRoute(barcode)) } ) }
            composable(
                route = Screen.Result.route,
                arguments = listOf(navArgument("barcode") { type = NavType.StringType })
            ) { backStackEntry ->
                ProductResultScreen(backStackEntry.arguments?.getString("barcode").orEmpty())
            }
            composable(Screen.Scanned.route) { ScannedProductScreen(navController = navController) }
            composable(Screen.Profile.route) { ProfileScreen(onDeleted = { }, authViewModel = authViewModel) }
        }
    }
}