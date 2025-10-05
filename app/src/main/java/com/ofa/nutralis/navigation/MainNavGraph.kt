package com.ofa.nutralis.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ofa.nutralis.R
import com.ofa.nutralis.ui.auth.AuthViewModel
import com.ofa.nutralis.ui.home.HomeScreen
import com.ofa.nutralis.ui.product.ProductCompareResultScreen
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
    val bottomScreens = listOf(Screen.Home, Screen.Scan, Screen.Scanned)
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
            Box {
                BottomBar(currentRoute, navController, bottomScreens)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-25).dp)
                ) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.Scan.route) },
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            focusedElevation = 0.dp,
                            hoveredElevation = 0.dp
                        ),
                        containerColor = Color(0xFF2E7D32),
                        modifier = Modifier
                            .size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.scan),
                            contentDescription = "Scan",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Scan",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                        fontSize = 13.sp
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(
                onProductClick = { barcode ->
                    navController.navigate(Screen.Result.createRoute(barcode))
                },
                onSearchClick = { navController.navigate(Screen.Input.route) },
                onCompareClick = { navController.navigate(Screen.Compare.route) },
            ) }
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
            composable(Screen.Compare.route) { ProductCompareScreen(onCompareClick = { barcode1, barcode2 -> navController.navigate(
                route = Screen.CompareResult.createRoute(barcode1, barcode2)) }) }
            composable(
                route = Screen.CompareResult.route,
                arguments = listOf(
                    navArgument("barcode1") { type = NavType.StringType },
                    navArgument("barcode2") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val barcode1 = backStackEntry.arguments?.getString("barcode1").orEmpty()
                val barcode2 = backStackEntry.arguments?.getString("barcode2").orEmpty()

                ProductCompareResultScreen(barcode1, barcode2)
            }
        }
    }
}