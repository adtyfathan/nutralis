package com.ofa.nutralis.ui.product
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ofa.nutralis.R

@Composable
    fun ProductInputScreen(
        viewModel: ProductInputViewModel,
        onProductClick: (String) -> Unit
    ) {
        val uiState by viewModel.uiState.collectAsState()
    val listState: LazyListState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val total = listState.layoutInfo.totalItemsCount
                if (
                    lastVisibleIndex != null &&
                    lastVisibleIndex >= total - 1 &&
                    uiState.hasMore &&
                    !uiState.isLoading
                ) {
                    viewModel.searchProducts(uiState.lastQuery, reset = false)
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            )
    ){
        when {
            uiState.isLoading && uiState.products.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFa9ffbe)
                )
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Unknown error",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }

            uiState.products.isEmpty() -> {
                if (uiState.lastQuery.isEmpty()){
                    Text(
                        text = "Search a product",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Text(
                        text = "No products found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.products) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onProductClick(product.code)
                                },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFf7f7f7)
                            )
                        ) {
                            Row (
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                AsyncImage(
                                    model = if (product.image_url.isNullOrEmpty()) {
                                        null
                                    } else {
                                        ImageRequest.Builder(LocalContext.current)
                                            .data(product.image_url)
                                            .crossfade(true)
                                            .build()
                                    },
                                    placeholder = painterResource(R.drawable.default_product),
                                    error = painterResource(R.drawable.default_product),
                                    fallback = painterResource(R.drawable.default_product),
                                    contentDescription = product.product_name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = if (product.product_name.isNullOrBlank()) "Unknown" else product.product_name,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when (product.nutrition_grades) {
                                                "a" -> Color(0xFF4CAF50)
                                                "b" -> Color(0xFF8BC34A)
                                                "c" -> Color(0xFFFFC107)
                                                "d" -> Color(0xFFFF9800)
                                                "e" -> Color(0xFFF44336)
                                                else -> Color.Gray
                                            }
                                        )
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if(product.nutrition_grades.isNullOrBlank() || product.nutrition_grades == "unknown") "-" else product.nutrition_grades,
                                        color = Color.White,
                                    )
                                }
                            }
                        }
                    }

                    if (uiState.products.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFFa9ffbe))
                            }
                        }
                    }
                }
            }
        }
    }
}