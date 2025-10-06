package com.ofa.nutralis.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ofa.nutralis.R
import com.ofa.nutralis.navigation.Screen
import com.ofa.nutralis.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopbar(
    currentRoute: String,
    navController: NavController,
    onProfileClick: () -> Unit,
    authViewModel: AuthViewModel,
){
    val user by authViewModel.userData.collectAsState()
    val state by authViewModel.uiState.collectAsState()

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF78C841),
        ),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable {
                        navController.navigate(Screen.Input.route)
                    }
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search, contentDescription = "search",
                        tint = Color.Black,
                    )
                    Text(
                        text = "Search a product...",
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 16.sp
                    )
                }
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFF78C841),
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = authViewModel.getAvatarResource(user?.avatar)),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { onProfileClick() },
                        contentScale = ContentScale.Crop,
                    )
                }
            }

        }
    )
}