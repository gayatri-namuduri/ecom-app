package com.gayatri.e_commerce_application.presentation.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gayatri.e_commerce_application.R
import com.gayatri.e_commerce_application.domain.models.CartDataModels
import com.gayatri.e_commerce_application.presentation.navigation.Routes
import com.gayatri.e_commerce_application.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EachProductDetailsScreenUi(navController: NavController, productId: String, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    val getProductById = viewModel.getProductByIDState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    var selectedSize by remember { mutableStateOf( "") }
    var quantity by remember { mutableStateOf( 1) }
    var isFavorite by remember { mutableStateOf( false)}

    LaunchedEffect (key1 = Unit) {
        viewModel.getProductByID(productId)
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    )   {innerpadding ->
            when{
                getProductById.value.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                getProductById.value.errorMessage != null -> {
                    Text(text = getProductById.value.errorMessage!!)
                }
                getProductById.value.userData != null -> {
                    val product = getProductById.value.userData!!.copy(productId = productId)
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerpadding)
                            .verticalScroll(rememberScrollState())
                    ){
                        Box(modifier = Modifier.height(300.dp)) {
                            AsyncImage(
                                model = product.image,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = product.name,
                                style = MaterialTheme. typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Rs ${product.price}",
                                style = MaterialTheme. typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            if(product.category=="Men's Store"||product.category=="Women's Store") {
                                Text(
                                    text = "Size",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("S", "M", "L", "XL").forEach { size ->
                                        OutlinedButton(
                                            onClick = { selectedSize = size },
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = if (selectedSize == size) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                contentColor = if (selectedSize == size) Color.White else MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text("${size}")
                                        }
                                    }
                                }
                            }
                            Text(
                                text = "Quantity",
                                style = MaterialTheme. typography.labelLarge,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                IconButton(onClick = { if (quantity > 1)quantity--}) {
                                    Text("-", style = MaterialTheme.typography.headlineSmall)
                                 }
                                Text(quantity. toString(), style = MaterialTheme.typography.bodyLarge)
                                IconButton(onClick = { quantity++}) {
                                    Text("+", style = MaterialTheme.typography.headlineSmall)
                                }
                            }
                            Text(
                                text = "Description",
                                style = MaterialTheme. typography.labelLarge,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )
                            Text(text = product.description)
                            Button(
                                onClick = {
                                    val cartDataModels = CartDataModels (
                                        name = product.name,
                                        image = product. image,
                                        price = product.price,
                                        quantity = quantity. toString(),
                                        size = selectedSize,
                                        productId = product.productId,
                                        description = product. description,
                                        category = product.category
                                    )
                                    viewModel. addToCart(cartDataModels =cartDataModels)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.darkGrey))
                            ) {
                                Text("Add to Cart")
                            }
                            Button (
                                onClick = {
                                    navController.navigate(Routes.CheckoutScreen(productId))
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(colorResource (id = R. color.darkGrey))
                            ){
                                Text("Buy Now")
                            }
                            OutlinedButton(
                                onClick = { isFavorite =! isFavorite
                                    viewModel.addToFav(product)
                                },
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                            ) {
                                Row {
                                    Icon (
                                        if (isFavorite) Icons. Default. Favorite else Icons. Default. FavoriteBorder,
                                        contentDescription = "Favorite",
                                    )
                                    Text (  "Add to Wishlist")

                                }
                            }
                        }
                    }

                }
            }
        }
}