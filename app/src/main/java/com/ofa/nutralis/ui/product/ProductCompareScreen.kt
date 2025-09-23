package com.ofa.nutralis.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ofa.nutralis.R
import com.ofa.nutralis.ui.util.SearchPopup

@Composable
fun ProductCompareScreen(
    viewModel: ProductCompareViewModel = hiltViewModel()
) {
    val product1 by viewModel.selectedProduct1.collectAsState()
    val product2 by viewModel.selectedProduct2.collectAsState()

    var showPopup by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (product1 != null){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.clearSearchResults()
                            showPopup = 1
                        },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFf7f7f7)
                    )
                ){
                    Row (
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = if (product1?.image_url.isNullOrEmpty()) {
                                null
                            } else {
                                ImageRequest.Builder(LocalContext.current)
                                    .data(product1?.image_url)
                                    .crossfade(true)
                                    .build()
                            },
                            placeholder = painterResource(R.drawable.default_product),
                            error = painterResource(R.drawable.default_product),
                            fallback = painterResource(R.drawable.default_product),
                            contentDescription = product1?.product_name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            if (product1?.product_name.isNullOrBlank()) "Unknown" else product1?.product_name?.let {
                                Text(
                                    text = it,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (product1?.nutrition_grades) {
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
                            if(product1?.nutrition_grades.isNullOrBlank() || product1?.nutrition_grades == "unknown") "-" else product1?.nutrition_grades?.let {
                                Text(
                                    text = it,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.clearSearchResults()
                            showPopup = 1 },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFf7f7f7)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .size(60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Select a product",
                            color = Color.Gray
                        )
                    }
                }
            }

            if (product2 != null){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.clearSearchResults()
                            showPopup = 2
                        },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFf7f7f7)
                    )
                ){
                    Row (
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = if (product2?.image_url.isNullOrEmpty()) {
                                null
                            } else {
                                ImageRequest.Builder(LocalContext.current)
                                    .data(product2?.image_url)
                                    .crossfade(true)
                                    .build()
                            },
                            placeholder = painterResource(R.drawable.default_product),
                            error = painterResource(R.drawable.default_product),
                            fallback = painterResource(R.drawable.default_product),
                            contentDescription = product2?.product_name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            if (product2?.product_name.isNullOrBlank()) "Unknown" else product2?.product_name?.let {
                                Text(
                                    text = it,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (product2?.nutrition_grades) {
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
                            if(product2?.nutrition_grades.isNullOrBlank() || product2?.nutrition_grades == "unknown") "-" else product2?.nutrition_grades?.let {
                                Text(
                                    text = it,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.clearSearchResults()
                            showPopup = 2 },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFf7f7f7)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .size(60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Select a product",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    if (showPopup != null) {
        SearchPopup(
            viewModel = viewModel,
            onDismiss = { showPopup = null },
            onProductSelected = { product ->
                viewModel.selectProduct(showPopup!!, product)
                showPopup = null
            }
        )
    }
}