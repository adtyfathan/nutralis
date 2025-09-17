package com.ofa.nutralis.ui.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ofa.nutralis.data.model.ScannedProduct

@Composable
fun ScannedProductItem(
    scannedProduct: ScannedProduct,
    onCardClick: (String) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onCardClick(scannedProduct.code) }),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(scannedProduct.image_url)
                    .crossfade(true)
                    .build(),
                contentDescription = scannedProduct.product_name,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 12.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    scannedProduct.product_name ?: "Unknown",
                    fontWeight = FontWeight.Bold
                    )
                Text(scannedProduct.product_type ?: "Unknown")
                Text(scannedProduct.nutriscore_grade ?: "Unknown")
                Text(scannedProduct.nutriscore_score?.toString() ?: "Unknown")
            }
        }
    }
}