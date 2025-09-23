package com.ofa.nutralis.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ofa.nutralis.ui.auth.AuthViewModel
import com.ofa.nutralis.ui.home.HomeScreen
import com.ofa.nutralis.ui.product.ProductCompareScreen
import com.ofa.nutralis.ui.product.ProductInputScreen
import com.ofa.nutralis.ui.product.ProductInputViewModel
import com.ofa.nutralis.ui.product.ProductResultScreen
import com.ofa.nutralis.ui.product.ProductScanScreen
import com.ofa.nutralis.ui.product.ScannedProductScreen
import com.ofa.nutralis.ui.profile.ProfileScreen
import com.ofa.nutralis.ui.util.BottomBar
import com.ofa.nutralis.ui.util.MainTopbar
import com.ofa.nutralis.ui.util.SearchTopbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    authViewModel: AuthViewModel,
    productInputViewModel: ProductInputViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val bottomScreens = listOf(Screen.Home, Screen.Scan, Screen.Scanned, Screen.Compare)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()

    Scaffold (
        topBar = {
            when {
                currentRoute == Screen.Input.route -> {
                    SearchTopbar(
                        onBack = { navController.popBackStack() },
                        onSearch = { query ->
                            productInputViewModel.searchProducts(query)
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
            composable(Screen.Input.route) { ProductInputScreen(
                productInputViewModel,
                onProductClick = { barcode ->
                    navController.navigate(Screen.Result.createRoute(barcode))
                }
            )}
            composable(
                route = Screen.Result.route,
                arguments = listOf(navArgument("barcode") { type = NavType.StringType })
            ) { backStackEntry ->
                ProductResultScreen(backStackEntry.arguments?.getString("barcode").orEmpty())
            }
            composable(Screen.Scanned.route) { ScannedProductScreen(navController = navController) }
            composable(Screen.Profile.route) { ProfileScreen(onDeleted = { }, authViewModel = authViewModel) }
            composable(Screen.Compare.route) { ProductCompareScreen() }
        }
    }
}