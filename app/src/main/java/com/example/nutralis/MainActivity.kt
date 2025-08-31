package com.example.nutralis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.nutralis.navigation.Screen
import com.example.nutralis.ui.auth.*
import com.example.nutralis.ui.home.HomeScreen
import com.example.nutralis.ui.product.*
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
                val bottomScreens = listOf(Screen.Home, Screen.Scan, Screen.Input, Screen.Scanned)
                val auth = FirebaseAuth.getInstance()
                val authViewModel: AuthViewModel = hiltViewModel()

                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()
                val showBars = currentRoute !in listOf(Screen.Login.route, Screen.Register.route)

                Scaffold(
                    topBar = {
                        if (showBars) {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary
                                ),
                                title = {
                                    Text("Nutralis", maxLines = 1, overflow = TextOverflow.Ellipsis)
                                },
                                actions = {
                                    IconButton(
                                        onClick = {
                                            authViewModel.logout()
                                            navController.navigate(Screen.Login.route) {
                                                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                                launchSingleTop = true
                                            }
                                        }
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBars) {
                            NavigationBar {
                                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()
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
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splash") {
                            val currentUser = auth.currentUser
                            LaunchedEffect(Unit) {
                                val destination = if (currentUser != null) Screen.Home.route else Screen.Login.route
                                navController.navigate(destination) {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) { CircularProgressIndicator() }
                        }

                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = { navController.navigate(Screen.Home.route) },
                                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
                            )
                        }

                        composable(Screen.Home.route) { HomeScreen() }

                        composable(Screen.Scan.route) {
                            ProductScanScreen { barcode ->
                                navController.navigate(Screen.Result.createRoute(barcode))
                            }
                        }

                        composable(Screen.Input.route) {
                            ProductInputScreen { barcode ->
                                navController.navigate(Screen.Result.createRoute(barcode))
                            }
                        }

                        composable(
                            route = Screen.Result.route,
                            arguments = listOf(navArgument("barcode") { type = NavType.StringType })
                        ) { backStackEntry ->
                            ProductResultScreen(backStackEntry.arguments?.getString("barcode").orEmpty())
                        }

                        composable(Screen.Scanned.route) {
                            ScannedProductScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
