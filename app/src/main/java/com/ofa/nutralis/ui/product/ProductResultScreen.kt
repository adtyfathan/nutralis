package com.ofa.nutralis.ui.product

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun ProductResultScreen(
    barcode: String,
    viewModel: ProductViewModel = hiltViewModel()
) {
    LaunchedEffect(barcode) {
        viewModel.fetchProduct(barcode)
    }

    val state = viewModel.state

    val primaryGreen = Color(0xFF4CAF50)
    val lightGreen = Color(0xFF81C784)
    val backgroundColor = Color(0xFFF8F9FA)
    val cardBackground = Color.White
    val textPrimary = Color(0xFF2E2E2E)
    val textSecondary = Color(0xFF666666)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = primaryGreen,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Error",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                state.error,
                                style = MaterialTheme.typography.bodyMedium,
                                color = textSecondary
                            )
                        }
                    }
                }
            }
            state.product != null -> {
                val product = state.product
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Product Image
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardBackground),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(16.dp)
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
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Basic Product Info
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardBackground),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    "Informasi Produk",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = primaryGreen,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                InfoRow("Barcode", barcode, textPrimary, textSecondary)
                                InfoRow("Nama Produk", product.product_name, textPrimary, textSecondary)
                                InfoRow("Grade Nutrisi", product.nutriscore_grade, textPrimary, textSecondary)
                                InfoRow("Skor Nutrisi", product.nutriscore_score?.toString(), textPrimary, textSecondary)
                                InfoRow("Tipe Produk", product.product_type, textPrimary, textSecondary)
                                InfoRow("Packaging", product.packaging, textPrimary, textSecondary)
                                InfoRow("Negara Asal", product.countries, textPrimary, textSecondary)
                            }
                        }
                    }

                    // Categories
                    if (!product.categories_tags.isNullOrEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = cardBackground),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        "Kategori Produk",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = primaryGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    product.categories_tags?.forEach { category ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .background(lightGreen, RoundedCornerShape(3.dp))
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                category,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = textPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Nutrient Levels
                    product.nutrient_levels?.let { nutrientLevels ->
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = cardBackground),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        "Tingkat Nutrisi",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = primaryGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    InfoRow("Lemak", nutrientLevels.fat, textPrimary, textSecondary)
                                    InfoRow("Garam", nutrientLevels.salt, textPrimary, textSecondary)
                                    InfoRow("Lemak Olahan", nutrientLevels.`saturated-fat`, textPrimary, textSecondary)
                                    InfoRow("Gula", nutrientLevels.sugars, textPrimary, textSecondary)
                                }
                            }
                        }
                    }

                    // Nutriments
                    product.nutriments?.let { nutriments ->
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = cardBackground),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        "Daftar Nutrisi",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = primaryGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    NutrientRow("Karbohidrat", nutriments.carbohydrates, nutriments.carbohydrates_unit, textPrimary, textSecondary)
                                    NutrientRow("Energi", nutriments.energy, nutriments.energy_unit, textPrimary, textSecondary)
                                    NutrientRow("Lemak", nutriments.fat, nutriments.fat_unit, textPrimary, textSecondary)
                                    NutrientRow("Protein", nutriments.proteins, nutriments.proteins_unit, textPrimary, textSecondary)
                                    NutrientRow("Garam", nutriments.salt, nutriments.salt_unit, textPrimary, textSecondary)
                                    NutrientRow("Lemak Olahan", nutriments.`saturated-fat`, "g", textPrimary, textSecondary)
                                    NutrientRow("Sodium", nutriments.sodium, nutriments.sodium_unit, textPrimary, textSecondary)
                                    NutrientRow("Gula", nutriments.sugars, nutriments.sugars_unit, textPrimary, textSecondary)
                                }
                            }
                        }
                    }

                    // Ingredients
                    if (!product.ingredients.isNullOrEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = cardBackground),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        "Komposisi Bahan",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = primaryGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    product.ingredients?.forEach { ingredient ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .background(lightGreen, RoundedCornerShape(3.dp))
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    ingredient.text ?: "Unknown",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = textPrimary
                                                )
                                                ingredient.percent_estimate?.let { percent ->
                                                    Text(
                                                        "${percent}%",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = textSecondary,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Allergens
                    if (!product.allergens_hierarchy.isNullOrEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = cardBackground),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        "Peringatan Alergen",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Color(0xFFFF5722),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    product.allergens_hierarchy?.forEach { allergen ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .background(Color(0xFFFF5722), RoundedCornerShape(3.dp))
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                allergen,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = textPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Bottom padding
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String?,
    textPrimary: Color,
    textSecondary: Color
) {
    if (value != null) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = textSecondary,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                color = textPrimary,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun NutrientRow(
    label: String,
    value: Any?,
    unit: String?,
    textPrimary: Color,
    textSecondary: Color
) {
    if (value != null) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = textSecondary,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                if (unit != null) "$value $unit" else value.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = textPrimary,
                fontWeight = FontWeight.Normal
            )
        }
    }
}