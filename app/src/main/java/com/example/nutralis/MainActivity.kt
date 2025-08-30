package com.example.nutralis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutralis.navigation.Screen
import com.example.nutralis.ui.auth.AuthViewModel
import com.example.nutralis.ui.auth.LoginScreen
import com.example.nutralis.ui.auth.RegisterScreen
import com.example.nutralis.ui.home.HomeScreen
import com.example.nutralis.ui.product.ProductInputScreen
import com.example.nutralis.ui.product.ProductResultScreen
import com.example.nutralis.ui.product.ProductScanScreen
import com.example.nutralis.ui.product.ScannedProductScreen
import com.example.nutralis.ui.theme.NutralisTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutralisTheme {
                val navController = rememberNavController()
                val bottomScreen = listOf(Screen.Home, Screen.Scan, Screen.Input, Screen.Scanned)
                val auth = FirebaseAuth.getInstance()
                val authViewModel: AuthViewModel = hiltViewModel()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route.orEmpty()

                val showBars = currentRoute != Screen.Login.route &&
                        currentRoute != Screen.Register.route

                Scaffold(
                    topBar = {
                        if (showBars){
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary
                                ),
                                title = {
                                    Text(
                                        "Nutralis",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                actions = {
                                    IconButton(
                                        onClick = {
                                            authViewModel.logout()
                                            navController.navigate(Screen.Login.route){
                                                popUpTo(navController.graph.findStartDestination().id){
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                            contentDescription = "Logout"
                                        )
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBars){
                            NavigationBar {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentRoute = navBackStackEntry?.destination?.route.orEmpty()

                                bottomScreen.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { screen.icon?.let { Icon(it, contentDescription = screen.label) } },
                                        label = { Text(screen.label) },
                                        selected = currentRoute.startsWith(screen.route.removeSuffix("/{barcode}")),
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (auth.currentUser != null) Screen.Home.route else Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login"){
                            LoginScreen(
                                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                            )
                        }

                        composable("register"){
                            RegisterScreen(
                                onRegisterSuccess = { navController.navigate(Screen.Home.route) },
                                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
                            )
                        }

                        composable(Screen.Home.route){
                            HomeScreen()
                        }

                        composable(Screen.Scan.route){
                            ProductScanScreen(onBarcodeScanned = { barcode ->
                                navController.navigate(Screen.Result.createRoute(barcode))
                            })
                        }

                        composable(Screen.Input.route){
                            ProductInputScreen(onSubmit = { barcode ->
                                navController.navigate(Screen.Result.createRoute(barcode))
                            })
                        }

                        composable(
                            route = Screen.Result.route,
                            arguments = listOf(navArgument("barcode") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                            ProductResultScreen(barcode = barcode)
                        }

                        composable(Screen.Scanned.route){
                            ScannedProductScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}