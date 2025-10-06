package com.ofa.nutralis.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ofa.nutralis.R
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFf7f7f7)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = if (scannedProduct.image_url.isNullOrEmpty()) {
                    null
                } else {
                    ImageRequest.Builder(LocalContext.current)
                        .data(scannedProduct.image_url)
                        .crossfade(true)
                        .build()
                },
                placeholder = painterResource(R.drawable.default_product),
                error = painterResource(R.drawable.default_product),
                fallback = painterResource(R.drawable.default_product),
                contentDescription = scannedProduct.product_name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (scannedProduct.product_name.isNullOrBlank()) "Unknown" else scannedProduct.product_name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                    )
            }

            val grade = scannedProduct.nutriscore_grade?.lowercase()
            val (gradeText, gradeColor) = when (grade) {
                "a" -> "A" to Color(0xFF53C406)
                "b" -> "B" to Color(0xFF78C841)
                "c" -> "C" to Color(0xFFF5D800)
                "d" -> "D" to Color(0xFFF5BB00)
                "e" -> "E" to Color(0xFFEB1B00)
                else -> "-" to Color.Gray
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
//                    .clip(RoundedCornerShape(8.dp))
                    .size(36.dp)
                    .background(gradeColor, shape = CircleShape)
//                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                androidx.compose.material.Text(
                    text = gradeText,
                    color = Color.White,
                )
            }
        }
    }
}