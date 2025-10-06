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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
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
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
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
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                val grade = product.nutriscore_grade?.lowercase()
                                val (gradeText, gradeColor) = when (grade) {
                                    "a" -> "A" to Color(0xFF4CAF50)
                                    "b" -> "B" to Color(0xFF8BC34A)
                                    "c" -> "C" to Color(0xFFFFC107)
                                    "d" -> "D" to Color(0xFFFF9800)
                                    "e" -> "E" to Color(0xFFF44336)
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
                                    "Product Information",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = primaryGreen,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                InfoRow("Barcode", barcode, textPrimary, textSecondary)
                                InfoRow("Product Name", product.product_name, textPrimary, textSecondary)
                                InfoRow("Nutrition Score", "${product.nutriscore_score?.toString()}/100", textPrimary, textSecondary)
                                InfoRow("Packaging", product.packaging, textPrimary, textSecondary)
                                InfoRow("Manufacturer", product.countries, textPrimary, textSecondary)
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
                                        "Product Categories",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = primaryGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    product.categories_tags?.forEach { category ->
                                        val formattedCategory = category.substringAfter(":")
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
                                                formattedCategory,
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
                    if (
                        listOf(
                            product.nutrient_levels?.fat,
                            product.nutrient_levels?.salt,
                            product.nutrient_levels?.`saturated-fat`,
                            product.nutrient_levels?.sugars
                        ).any { !it.isNullOrEmpty() }
                    ) {
                        product.nutrient_levels.let { nutrientLevels ->
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
                                            "Nutrient Levels",
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = primaryGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))

                                        NutrientLevelsRow("Fat",nutrientLevels?.fat)
                                        NutrientLevelsRow("Salt",nutrientLevels?.salt)
                                        NutrientLevelsRow("Saturated Fat",nutrientLevels?.`saturated-fat`)
                                        NutrientLevelsRow("Sugars",nutrientLevels?.sugars)
                                    }
                                }
                            }
                        }
                    }

                    // Nutriments
                    if (listOf(
                            product.nutriments?.carbohydrates,
                            product.nutriments?.energy,
                            product.nutriments?.fat,
                            product.nutriments?.proteins,
                            product.nutriments?.salt,
                            product.nutriments?.`saturated-fat`,
                            product.nutriments?.sodium,
                            product.nutriments?.sugars
                        ).any { it != null }
                    ) {
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
                                            "Nutrients",
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = primaryGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))

                                        InfoRow("Carbohydrates", "${nutriments.carbohydrates ?: "-"} ${nutriments.carbohydrates_unit ?: ""}", textPrimary, textSecondary)
                                        InfoRow("Energy", "${nutriments.energy ?: "-"} ${nutriments.energy_unit ?: ""}", textPrimary, textSecondary)
                                        InfoRow("Fat", "${nutriments.fat ?: "-"} ${nutriments.fat_unit ?: ""}", textPrimary, textSecondary)
                                        InfoRow("Protein", "${nutriments.proteins ?: "-"} ${nutriments.proteins_unit ?: ""}", textPrimary, textSecondary)
                                        InfoRow("Salt", "${nutriments.salt ?: "-"} ${nutriments.salt_unit ?: ""}", textPrimary, textSecondary)
                                        InfoRow("Saturated Fat", "${nutriments.`saturated-fat` ?: "-"} g", textPrimary, textSecondary)
                                        InfoRow("Sodium", "${nutriments.sodium ?: "-"} ${nutriments.sodium_unit ?: ""}", textPrimary, textSecondary)
                                        InfoRow("Sugars", "${nutriments.sugars ?: "-"} ${nutriments.sugars_unit ?: ""}", textPrimary, textSecondary)
                                    }
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
                                        "Ingredients",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = primaryGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    product.ingredients?.forEach { ingredient ->
                                        InfoRow("${ingredient.text}", "${ingredient.percent_estimate}%", textPrimary, textSecondary)
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
                                        "Allergen Warning",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Color(0xFFFF5722),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    product.allergens_hierarchy?.forEach { allergen ->
                                        val formattedAllergen = allergen.substringAfter(":")
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
                                                formattedAllergen,
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
    val formattedValue = value?.substringAfter(":")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = textSecondary,
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Top) // left side
        )
        Text(
            text = if (formattedValue.isNullOrEmpty()) "Unknown" else formattedValue,
            style = MaterialTheme.typography.bodyMedium,
            color = textPrimary,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f), // right side
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun NutrientLevelsRow(
    label: String,
    value: String?,
){
    val nutrientLevelColor = when (value) {
        "low" -> Color(0xFF1df705)
        "moderate" -> Color(0xFFebf705)
        "high" -> Color(0xFFf73105)
        else -> Color(0xFF212121)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF666666),
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Top) // left side
        )
        Text(
            text = value ?: "Unknown",
            style = MaterialTheme.typography.bodyMedium,
            color = nutrientLevelColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f), // right side
            textAlign = TextAlign.Start
        )
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