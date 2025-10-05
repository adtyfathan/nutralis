package com.ofa.nutralis.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ofa.nutralis.R
import com.ofa.nutralis.ui.auth.AuthViewModel
import com.ofa.nutralis.ui.auth.LoginScreen
import com.ofa.nutralis.ui.auth.RegisterScreen

@Composable
fun GuestNavGraph (
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    GuestBackground {
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
}

@Composable
fun GuestBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.auth_background),
                contentScale = ContentScale.FillBounds
            )
    ) {
        content()
    }
}
