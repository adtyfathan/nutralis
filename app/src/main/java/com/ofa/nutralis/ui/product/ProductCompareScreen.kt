package com.ofa.nutralis.ui.product

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ofa.nutralis.R
import com.ofa.nutralis.data.remote.ProductItem
import com.ofa.nutralis.ui.util.SearchPopup

@Composable
fun ProductCompareScreen(
    viewModel: ProductCompareViewModel = hiltViewModel(),
    onCompareClick: (String, String) -> Unit
) {
    val product1 by viewModel.selectedProduct1.collectAsState()
    val product2 by viewModel.selectedProduct2.collectAsState()

    var showPopup by remember { mutableStateOf<Int?>(null) }

    // Color scheme
    val primaryGreen = Color(0xFF78C841)
    val lightGreen = Color(0xFF4CAF50)
    val surfaceGreen = Color(0xFFF1F8E9)
    val backgroundColor = Color(0xFFFAFAFA)
    val cardWhite = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Compare Products",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Select two products to compare",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Product Selection Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Product 1 Card
                ProductSelectionCard(
                    product = product1,
                    primaryGreen = primaryGreen,
                    lightGreen = lightGreen,
                    cardWhite = cardWhite,
                    surfaceGreen = surfaceGreen,
                    onCardClick = {
                        viewModel.clearSearchResults()
                        showPopup = 1
                    }
                )

                // VS Divider
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(lightGreen, primaryGreen)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VS",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Product 2 Card
                ProductSelectionCard(
                    product = product2,
                    primaryGreen = primaryGreen,
                    lightGreen = lightGreen,
                    cardWhite = cardWhite,
                    surfaceGreen = surfaceGreen,
                    onCardClick = {
                        viewModel.clearSearchResults()
                        showPopup = 2
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Compare Button
            Button(
                onClick = {
                    if (product1 != null && product2 != null) {
                        onCompareClick(product1!!.code, product2!!.code)
                    }
                },
                enabled = product1 != null && product2 != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = if (product1 != null && product2 != null) 8.dp else 0.dp,
                        shape = RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (product1 != null && product2 != null) primaryGreen else Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Start Comparison",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Search Popup
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
}

@Composable
private fun ProductSelectionCard(
    product: ProductItem?,
    primaryGreen: Color,
    lightGreen: Color,
    cardWhite: Color,
    surfaceGreen: Color,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardWhite
        )
    ) {
        if (product != null) {
            // Selected Product Content
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Product Image
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
                            .size(70.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                2.dp,
                                surfaceGreen,
                                RoundedCornerShape(16.dp)
                            )
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Product Name
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (product.product_name.isNullOrEmpty()) "Unknown Product" else product.product_name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF212121),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        } else {
            // Empty State Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tap to choose a product",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}