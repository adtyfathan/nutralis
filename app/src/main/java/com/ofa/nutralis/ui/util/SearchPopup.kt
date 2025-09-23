package com.ofa.nutralis.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ofa.nutralis.R
import com.ofa.nutralis.data.remote.ProductItem
import com.ofa.nutralis.ui.product.ProductCompareViewModel

@Composable
fun SearchPopup(
    viewModel: ProductCompareViewModel,
    onDismiss: () -> Unit,
    onProductSelected: (ProductItem) -> Unit
) {
    val uiState by viewModel.searchUiState.collectAsState()
    val listState = rememberLazyListState()
    var query by remember { mutableStateOf("") }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val total = listState.layoutInfo.totalItemsCount
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= total - 1 &&
                    uiState.hasMore &&
                    !uiState.isLoading
                ) {
                    viewModel.searchProducts(uiState.lastQuery, reset = false)
                }
            }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f) // take ~85% of height
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header with search bar + close button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Search product") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (query.isNotBlank()) {
                                        viewModel.searchProducts(query, reset = true)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            }
                        }
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Close"
                        )
                    }
                }

                // Content
                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        uiState.isLoading && uiState.products.isEmpty() -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color(0xFF4CAF50)
                            )
                        }
                        uiState.error != null -> {
                            Text(
                                text = uiState.error ?: "Something went wrong",
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Red
                            )
                        }
                        uiState.products.isEmpty() -> {
                            Text(
                                text = if (uiState.lastQuery.isEmpty())
                                    "Type something to start searching"
                                else
                                    "No products found",
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Gray
                            )
                        }
                        else -> {
                            LazyColumn(
                                state = listState,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                contentPadding = PaddingValues(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(uiState.products) { product ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onProductSelected(product) },
                                        shape = RoundedCornerShape(16.dp),
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(product.image_url)
                                                    .crossfade(true)
                                                    .build(),
                                                placeholder = painterResource(R.drawable.default_product),
                                                error = painterResource(R.drawable.default_product),
                                                contentDescription = product.product_name,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                            )
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = product.product_name
                                                        ?: "Unknown product",
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
                                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = product.nutrition_grades
                                                        ?.takeIf { it != "unknown" }
                                                        ?.uppercase()
                                                        ?: "-",
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }

                                if (uiState.isLoading) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = Color(0xFF4CAF50))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

