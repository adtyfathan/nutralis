package com.ofa.nutralis.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.ofa.nutralis.data.remote.Product

// Color palette
private val PrimaryGreen = Color(0xFF2E7D32)
private val LightGreen = Color(0xFF4CAF50)
private val VeryLightGreen = Color(0xFFE8F5E8)
private val SoftWhite = Color(0xFFFAFAFA)
private val BorderGray = Color(0xFFE0E0E0)
private val TextDark = Color(0xFF212121)
private val TextGray = Color(0xFF757575)

@Composable
fun ProductCompareResultScreen(
    barcode1: String,
    barcode2: String,
    viewModel: ProductCompareResultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProducts(barcode1, barcode2)
    }

    when {
        uiState.isLoading -> LoadingState()
        uiState.error != null -> ErrorState(uiState.error!!)
        uiState.product1 != null && uiState.product2 != null -> CompareContent(
            product1 = uiState.product1!!.product,
            product2 = uiState.product2!!.product
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftWhite),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = LightGreen,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Comparing products...",
                color = TextGray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftWhite),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            backgroundColor = Color.White,
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Error",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    message,
                    fontSize = 14.sp,
                    color = TextGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CompareContent(product1: Product, product2: Product) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftWhite),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Product cards header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ProductCard(product1)
                ProductCard(product2)
            }
        }

        // Nutrient Levels Section
        item {
            ComparisonSection(
                title = "Nutrient Levels",
                product1 = product1,
                product2 = product2
            ) {
                comparisonItem("Fat", formatLevel(product1.nutrient_levels?.fat), formatLevel(product2.nutrient_levels?.fat))
                comparisonItem("Saturated Fat", formatLevel(product1.nutrient_levels?.`saturated-fat`), formatLevel(product2.nutrient_levels?.`saturated-fat`))
                comparisonItem("Sugars", formatLevel(product1.nutrient_levels?.sugars), formatLevel(product2.nutrient_levels?.sugars))
                comparisonItem("Salt", formatLevel(product1.nutrient_levels?.salt), formatLevel(product2.nutrient_levels?.salt))
            }
        }

        // Nutriments Section
        item {
            ComparisonSection(
                title = "Nutriments",
                product1 = product1,
                product2 = product2
            ) {
                comparisonItem("Nutri-Score", formatNutriScore(product1.nutriscore_grade), formatNutriScore(product2.nutriscore_grade))
                comparisonItem("Energy", formatNutrient(product1.nutriments?.energy, product1.nutriments?.energy_unit), formatNutrient(product2.nutriments?.energy, product2.nutriments?.energy_unit))
                comparisonItem("Fat", formatNutrient(product1.nutriments?.fat, product1.nutriments?.fat_unit), formatNutrient(product2.nutriments?.fat, product2.nutriments?.fat_unit))
                comparisonItem("Saturated Fat", formatNutrient(product1.nutriments?.`saturated-fat`, product1.nutriments?.`saturated-fat_unit`), formatNutrient(product2.nutriments?.`saturated-fat`, product2.nutriments?.`saturated-fat_unit`))
                comparisonItem("Sugars", formatNutrient(product1.nutriments?.sugars, product1.nutriments?.sugars_unit), formatNutrient(product2.nutriments?.sugars, product2.nutriments?.sugars_unit))
                comparisonItem("Salt", formatNutrient(product1.nutriments?.salt, product1.nutriments?.salt_unit), formatNutrient(product2.nutriments?.salt, product2.nutriments?.salt_unit))
                comparisonItem("Proteins", formatNutrient(product1.nutriments?.proteins, product1.nutriments?.proteins_unit), formatNutrient(product2.nutriments?.proteins, product2.nutriments?.proteins_unit))
            }
        }

        // Ingredients Section
        item {
            ComparisonSection(
                title = "Ingredients",
                product1 = product1,
                product2 = product2
            ) {
                comparisonItem(
                    "Ingredients",
                    product1.ingredients?.mapNotNull { it.text }?.let { ComparisonValue.ListItems(it) } ?: ComparisonValue.Text("-"),
                    product2.ingredients?.mapNotNull { it.text }?.let { ComparisonValue.ListItems(it) } ?: ComparisonValue.Text("-")
                )
            }
        }

        // Allergens Section
        item {
            ComparisonSection(
                title = "Allergens",
                product1 = product1,
                product2 = product2
            ) {
                comparisonItem(
                    "Allergens",
                    product1.allergens_hierarchy?.let { ComparisonValue.ListItems(it) } ?: ComparisonValue.Text("-"),
                    product2.allergens_hierarchy?.let { ComparisonValue.ListItems(it) } ?: ComparisonValue.Text("-")
                )
            }
        }
    }
}

@Composable
private fun ComparisonSection(
    title: String,
    product1: Product,
    product2: Product,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Section header with green accent
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        VeryLightGreen,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product name header row
            productNameHeaderRow(product1, product2)

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            content()
        }
    }
}

@Composable
private fun productNameHeaderRow(product1: Product, product2: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                SoftWhite,
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Content",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = product1.product_name ?: "Unknown",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = product2.product_name ?: "Unknown",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun comparisonItem(label: String, value1: ComparisonValue, value2: ComparisonValue) {
    ComparisonRow(label, value1, value2)
}

@Composable
private fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            ),
        backgroundColor = Color.White,
        elevation = 0.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Product image container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SoftWhite)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.image_url)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.default_product),
                    error = painterResource(R.drawable.default_product),
                    fallback = painterResource(R.drawable.default_product),
                    contentDescription = product.product_name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
            }


            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.product_name ?: "Unknown Product",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .height(40.dp)
            )

        }
    }
}

@Composable
private fun ComparisonRow(label: String, value1: ComparisonValue, value2: ComparisonValue) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryGreen,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                ComparisonValueContent(value1)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                ComparisonValueContent(value2)
            }
        }

        Divider(
            color = BorderGray.copy(alpha = 0.5f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
private fun ComparisonValueContent(value: ComparisonValue, modifier: Modifier = Modifier) {
    when (value) {
        is ComparisonValue.Text -> Text(
            text = value.value,
            fontSize = 13.sp,
            color = TextDark,
            fontWeight = FontWeight.Normal,
            modifier = modifier
        )

        is ComparisonValue.Level -> Text(
            text = value.level.replaceFirstChar { it.uppercase() },
            fontSize = 13.sp,
            color = getLevelColor(value.level),
            fontWeight = FontWeight.Medium,
            modifier = modifier
        )

        is ComparisonValue.NutriScore -> Box(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(getNutriScoreColor(value.grade))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = value.grade.uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        is ComparisonValue.ListItems -> RowList(value.items, modifier)
    }
}



@Composable
fun RowList(listData: List<String>, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        listData.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(LightGreen, RoundedCornerShape(2.dp))
                        .padding(top = 6.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item,
                    fontSize = 13.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

sealed class ComparisonValue {
    data class Text(val value: String) : ComparisonValue()
    data class ListItems(val items: List<String>) : ComparisonValue()
    data class Level(val level: String) : ComparisonValue()
    data class NutriScore(val grade: String) : ComparisonValue()
}



private fun formatNutrient(value: Double?, unit: String?): ComparisonValue =
    if (value != null && unit != null) ComparisonValue.Text("$value $unit") else ComparisonValue.Text("-")

private fun formatLevel(level: String?): ComparisonValue =
    if (!level.isNullOrBlank()) ComparisonValue.Level(level.lowercase()) else ComparisonValue.Text("-")

private fun getLevelColor(level: String): Color = when (level) {
    "low" -> Color(0xFF1df705)
    "moderate" -> Color(0xFFebf705)
    "high" -> Color(0xFFf73105)
    else -> TextDark
}

private fun formatNutriScore(grade: String?): ComparisonValue =
    if (!grade.isNullOrBlank() && grade != "unknown")
        ComparisonValue.NutriScore(grade.lowercase())
    else
        ComparisonValue.Text("-")

private fun getNutriScoreColor(grade: String): Color = when (grade) {
    "a" -> Color(0xFF4CAF50)
    "b" -> Color(0xFF8BC34A)
    "c" -> Color(0xFFFFC107)
    "d" -> Color(0xFFFF9800)
    "e" -> Color(0xFFF44336)
    else -> Color.Gray
}

