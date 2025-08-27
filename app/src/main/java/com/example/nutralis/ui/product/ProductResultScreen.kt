package com.example.nutralis.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ProductResultScreen(
    barcode: String,
    viewModel: ProductViewModel = hiltViewModel()
) {
    LaunchedEffect(barcode) {
        viewModel.fetchProduct(barcode)
    }

    val state = viewModel.state

    when {
        state.isLoading -> CircularProgressIndicator()
        state.error != null -> Text("Error: ${state.error}", color = Color.Red)
        state.product != null -> {
            val product = state.product
            LazyColumn(
                modifier =  Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    product.image_url?.let { url ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(product.image_url)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }

                item {
                    Text("Barcode produk: $barcode")
                    Text("Nama produk: ${product.product_name}")

                    Text("Grade nutrisi: ${product.nutriscore_grade}")
                    Text("Skor nutrisi: ${product.nutriscore_score}")

                    Text("Tipe produk: ${product.product_type}")
                    Text("Packaging: ${product.packaging}")
                    Text("Negara asal: ${product.countries}")
                }

                item {
                    Text("Kategori produk:")
                    product.categories_tags?.forEach { category ->
                        Text(" - ${category}")
                    }
                }

                item {
                    Text("Tingkat nutrisi:")
                    product.nutrient_levels?.let { nutrientLevels ->
                        Text("Lemak: ${nutrientLevels.fat}")
                        Text("Garam: ${nutrientLevels.salt}")
                        Text("Lemak olahan: ${nutrientLevels.`saturated-fat`}")
                        Text("Gula: ${nutrientLevels.sugars}")
                    }
                }

                item {
                    Text("Daftar nutrisi:")
                    product.nutriments?.let { nutriments ->
                        Text("Karbohidrat: ${nutriments.carbohydrates} - ${nutriments.carbohydrates_unit}")
                        Text("Energi: ${nutriments.energy} - ${nutriments.energy_unit}")
                        Text("Lemak: ${nutriments.fat} - ${nutriments.fat_unit}")
                        Text("Protein: ${nutriments.proteins} - ${nutriments.proteins_unit}")
                        Text("Garam: ${nutriments.salt} - ${nutriments.salt_unit}")
                        Text("Lemak olahan: ${nutriments.`saturated-fat`} - g")
                        Text("Sodium: ${nutriments.sodium} - ${nutriments.sodium_unit}")
                        Text("Gula: ${nutriments.sugars} - ${nutriments.sugars_unit}")
                    }
                }

                item {
                    Text("Komposisi bahan:")
                    product.ingredients?.forEach { ingredient ->
                        Text(" - ${ingredient.text} ${ingredient.percent_estimate}%")
                    }
                }

                item {
                    Text("Tidak baik untuk penderita alergi:")
                    product.allergens_hierarchy?.forEach { allergen ->
                        Text(" - ${allergen}")
                    }
                }
            }
        }
    }
}