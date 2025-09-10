package com.example.nutralis.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onDeleted: () -> Unit
){
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var newUsername by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    LaunchedEffect(user) {
        user?.let { newUsername = it.username }
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ){
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text("Error: $error")
            }
            user != null -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("Username") }
                    )

                    Button(
                        onClick = {
                            if (newUsername.isNotBlank()){
                                viewModel.updateUser(newUsername)
                            }
                        }
                    ) {
                        Text("Simpan")
                    }

                    Button(
                        onClick = {
                            viewModel.logout()
                        }
                    ) {
                        Text("Logout")
                    }

                    Button(
                        onClick = {
                            viewModel.deleteUser(onDeleted)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text("Hapus Akun")
                    }
                }
            }
            else -> {
                Text("No user data available")
            }
        }
    }
}