package com.example.nutralis.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
){
    val state by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFEBC894),
                        Color(0xFFB49EF4)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.6f))
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Login",
                color = Color.Black,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Enter your email and password to login",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            TextField(
                value = email,
                onValueChange = {email = it},
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 4.dp
                    )
                    .defaultMinSize(minHeight = 36.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 13.sp
                )
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 4.dp,
                        bottom = 8.dp
                    )
                    .defaultMinSize(minHeight = 36.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 13.sp
                )
            )

            Button(
                onClick = { viewModel.login(email, password) },
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D61E7),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp)
            ) {
                if(state.isLoading){
                    CircularProgressIndicator()
                } else {
                    Text(
                        "Login",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "No account?",
                    color = Color.Black,
                    fontSize = 13.sp
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        "Register here",
                        fontSize = 13.sp,
                        color = Color(0xFF1D61E7)
                    )
                }
            }

            state.error?.let {
                Text(it, color = Color.Red)
            }

            if(state.isSuccess){
                onLoginSuccess()
            }
        }
    }
}