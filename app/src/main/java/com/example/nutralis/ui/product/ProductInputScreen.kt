package com.example.nutralis.ui.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutralis.R

@Composable
    fun ProductInputScreen(
        viewModel: ProductInputViewModel = hiltViewModel(),
        onHistoryClick: (String) -> Unit
    ) {
    val products by viewModel.scannedProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProductsByUserId()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ){
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            products.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(products.size){ index ->
                        val product = products[index]
                        val productName = product.product_name ?: ""
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.clickable{
                                    onHistoryClick(product.code)
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.history),
                                    contentDescription = "history icon",
                                    modifier = Modifier
                                        .size(24.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(productName)
                            }
                    }
                }
            }
            else -> {
                Text("Belum ada pencarian")
            }
        }
    }
}