package com.example.nutralis.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nutralis.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(
    onDeleted: () -> Unit,
    authViewModel: AuthViewModel
){
    val user by authViewModel.userData.collectAsState()
    val state by authViewModel.uiState.collectAsState()
    val showAvatarPicker by authViewModel.showAvatarPicker.collectAsState()

    var newUsername by remember { mutableStateOf("") }
    var tempAvatar by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            newUsername = it.username
            tempAvatar = it.avatar
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.error != null -> {
                Text("Error: ${state.error}")
            }
            user != null -> {
                if (showAvatarPicker) {
                    Dialog(onDismissRequest = { authViewModel.closeAvatarPicker() }) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            tonalElevation = 8.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 300.dp, max = 500.dp)
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Title
                                Text(
                                    "Select Avatar",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Divider(
                                    modifier = Modifier
                                        .padding(vertical = 12.dp)
                                        .fillMaxWidth()
                                )

                                // Avatars in a grid
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(listOf("avatar1", "avatar2", "avatar3", "avatar4", "avatar5")) { avatar ->
                                        val isSelected = tempAvatar == avatar
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    width = if (isSelected) 3.dp else 0.dp,
                                                    color = if (isSelected) Color(0xFF4CAF50) else Color.Transparent,
                                                    shape = CircleShape
                                                )
                                                .clickable { tempAvatar = avatar }
                                        ) {
                                            Image(
                                                painter = painterResource(id = authViewModel.getAvatarResource(avatar)),
                                                contentDescription = avatar,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.size(70.dp).clip(CircleShape)
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))

                                // Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { authViewModel.closeAvatarPicker() }) {
                                        Text("Cancel")
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Button(onClick = {
                                        authViewModel.closeAvatarPicker()
                                    }) {
                                        Text("Select")
                                    }
                                }
                            }
                        }
                    }
                }


                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = authViewModel.getAvatarResource(tempAvatar)),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(148.dp)
                            .clip(CircleShape)
                            .clickable { authViewModel.openAvatarPicker() },
                        contentScale = ContentScale.Crop
                    )

                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFa9ffbe)),
                            onClick = {
                                if (newUsername.isNotBlank()){
                                    authViewModel.updateUser(newUsername, avatar = tempAvatar)
                                }
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            authViewModel.logout()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFa9ffbe))
                    ) {
                        Text("Logout")
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            authViewModel.deleteUser(onDeleted)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFff9e94))
                    ) {
                        Text("Delete Account")
                    }
                }
            }
            else -> {
                Text("No user data available")
            }
        }
    }
}