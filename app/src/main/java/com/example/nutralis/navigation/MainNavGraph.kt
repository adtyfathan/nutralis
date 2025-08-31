package com.example.nutralis.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val bottomScreens = listOf(Screen.Home, Screen.Scan, Screen.Input, Screen.Scanned)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text("Nutralis")
                },
                actions = {
                    IconButton(
                        onClick = {
                            authViewModel.logout()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        bottomBar = {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Scan.route) { ProductScanScreen { barcode -> navController.navigate(Screen.Result.createRoute(barcode)) } }
            composable(Screen.Input.route) { ProductInputScreen { barcode -> navController.navigate(Screen.Result.createRoute(barcode)) } }
            composable(
                route = Screen.Result.route,
                arguments = listOf(navArgument("barcode") { type = NavType.StringType })
            ) { backStackEntry ->
                ProductResultScreen(backStackEntry.arguments?.getString("barcode").orEmpty())
            }
            composable(Screen.Scanned.route) { ScannedProductScreen(navController = navController) }
        }
    }
}