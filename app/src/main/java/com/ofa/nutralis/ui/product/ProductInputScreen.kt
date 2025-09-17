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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
    fun ProductInputScreen(
        viewModel: ProductInputViewModel,
        onProductClick: (String) -> Unit
    ) {
        val uiState by viewModel.uiState.collectAsState()
    val listState: LazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ){
        when {
            uiState.isLoading -> {
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
                Text(
                    text = "No products found",
                    modifier = Modifier.align(Alignment.Center)
                )
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
                        ) {
                            Row (
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(product.image_url)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = product.product_name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column (
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = product.product_name ?: "Unknown",
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

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
                                            text = product.nutrition_grades?.uppercase() ?: "-",
                                            color = Color.White,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (uiState.hasMore) {
                        item {
                            Button(
                                onClick = {
                                    viewModel.searchProducts(
                                        query = uiState.lastQuery,
                                        reset = false
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(color = Color(0xFFa9ffbe)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Load more")
                            }
                        }
                    }
                }
            }
        }
    }
}