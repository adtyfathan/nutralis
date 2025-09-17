package com.ofa.nutralis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ofa.nutralis.navigation.GuestNavGraph
import com.ofa.nutralis.navigation.MainNavGraph
import com.ofa.nutralis.ui.auth.AuthViewModel
import com.ofa.nutralis.ui.theme.NutralisTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NutralisTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val user by authViewModel.currentUser.collectAsState()

                when (user) {
                    null -> GuestNavGraph(authViewModel = authViewModel)
                    else -> MainNavGraph(authViewModel = authViewModel)
                }
            }
        }
    }
}
