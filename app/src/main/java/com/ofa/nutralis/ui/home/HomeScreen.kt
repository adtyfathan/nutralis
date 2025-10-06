package com.ofa.nutralis.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ofa.nutralis.R
import com.ofa.nutralis.data.remote.ProductItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCompareClick: () -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val products by viewModel.products.collectAsState()

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Welcome to Nutralis, user!",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // adjust as needed
                ) {
                    // Background image
                    Image(
                        painter = painterResource(R.drawable.hero), // replace with your dark-toned image
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                    )

                    // Gradient overlay for readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Transparent
                                    ),
                                    startY = 0f,
                                    endY = 400f
                                )
                            )
                            .clip(RoundedCornerShape(20.dp))
                    )

                    // Text content
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Your smart companion",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Explore nutrition facts, product composition, and make healthier choices with ease.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            ),
                            maxLines = 3
                        )
                    }
                }
            }

            Text(
                text = "Quick Menu",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF78C841))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF78C841)
//                            brush = Brush.linearGradient(
//                                colors = listOf(
//                                    Color(0xFFd2f8e5),
//                                    Color(0xFFa9ffbe)
//                                ),
//                                start = Offset(0f, 0f),
//                                end = Offset(1000f, 1000f)
//                            )
                        )
                        .padding(16.dp)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .clickable {
                                        onSearchClick()
                                    }
                                    .weight(1f),
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.search),
                                    tint = Color.White,
                                    contentDescription = "Search",
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Search",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .clickable {
                                        onCompareClick()
                                    }
                                    .weight(1f)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.compare),
                                    tint = Color.White,
                                    contentDescription = "Compare",
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Compare",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

        }

        item {
            Text(
                text = "Explore by Category",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF78C841),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 1.dp)
            ) {
                items(viewModel.categories.size) { index ->
                    val category = viewModel.categories[index]
                    val isSelected = category == selectedCategory

                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectCategory(category) },
                        label = {
                            Text(
                                text = category,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = if (isSelected) Color(0xFF78C841) else Color.White,
                            labelColor = if (isSelected) Color.White else Color(0xFF78C841),
                            selectedContainerColor = Color(0xFF78C841),
                            selectedLabelColor = Color.White
                        ),
                        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFF78C841)),
                        elevation = FilterChipDefaults.filterChipElevation(
                            elevation = if (isSelected) 4.dp else 0.dp
                        )
                    )
                }
            }

        }

        item {
            Spacer(modifier = Modifier.height(12.dp))

            when {
                viewModel.isLoading.collectAsState().value -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF78C841))
                    }
                }
                products.isNotEmpty() -> {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        maxItemsInEachRow = 2
                    ) {
                        products.forEach { product ->
                            ProductCard(
                                product = product,
                                modifier = Modifier.weight(1f),
                                onProductClick = onProductClick
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ProductCard(product: ProductItem, modifier: Modifier, onProductClick: (String) -> Unit) {
    Card(
        modifier = modifier
            .clickable {
                onProductClick(product.code)
            }
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFf7f7f7)
        )
    ) {
        Column {
            Box {
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
                        .fillMaxWidth()
                        .height(150.dp)
                )

                val grade = product.nutrition_grades?.lowercase()
                val (gradeText, gradeColor) = when (grade) {
                    "a" -> "A" to Color(0xFF53C406)
                    "b" -> "B" to Color(0xFF78C841)
                    "c" -> "C" to Color(0xFFF5D800)
                    "d" -> "D" to Color(0xFFF5BB00)
                    "e" -> "E" to Color(0xFFEB1B00)
                    else -> "-" to Color.Gray
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .size(36.dp)
                        .background(gradeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = gradeText,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            }

            Text(
                text = if (product.product_name.isNullOrEmpty()) "Unknown Product" else product.product_name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                modifier = Modifier.padding(
                    vertical = 16.dp,
                    horizontal = 16.dp
                )
            )

        }
    }
}