package com.ofa.nutralis.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ofa.nutralis.navigation.Screen

@Composable
fun ScannedProductScreen(
    viewModel: ScannedProductViewModel = hiltViewModel(),
    navController: NavController
) {
    val products by viewModel.scannedProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProductsByUserId()
    }

    Box(
        modifier = Modifier.
            padding(16.dp)
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFa9ffbe))
                }
            }
            products.isEmpty() -> {
                Text("Belum ada produk")
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products.size) { index ->
                        val product = products[index]
                        ScannedProductItem(
                            scannedProduct = product,
                            onCardClick = { code ->
                                navController.navigate(Screen.Result.createRoute(code))
                            }
                        )
                    }
                }
            }
        }
    }
}