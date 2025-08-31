package com.example.nutralis.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutralis.ui.auth.AuthViewModel
import com.example.nutralis.ui.auth.LoginScreen
import com.example.nutralis.ui.auth.RegisterScreen

@Composable
fun GuestNavGraph (
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable("login"){
            LoginScreen (
                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable("register"){
            RegisterScreen (
                onRegisterSuccess = { navController.navigate(Screen.Home.route) },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }
    }
}