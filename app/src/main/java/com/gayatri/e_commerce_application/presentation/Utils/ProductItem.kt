package com.gayatri.e_commerce_application.presentation.Utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gayatri.e_commerce_application.domain.models.ProductDataModels


@Composable
fun ProductItem (
    product: ProductDataModels,
    onProductClick: () -> Unit ){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onProductClick
            ),
 //       elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        shape= RoundedCornerShape(8.dp)
    ){
        Column {
            AsyncImage(model = product. image, contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .background (color = Color. White)
                    .height (200. dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Fit
            )
            Column (
                modifier = Modifier.padding (8.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${product.finalPrice}/-",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }


    }
}
