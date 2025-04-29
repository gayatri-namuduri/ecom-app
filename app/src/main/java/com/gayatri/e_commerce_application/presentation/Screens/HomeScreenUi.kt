package com.gayatri.e_commerce_application.presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gayatri.e_commerce_application.R
import com.gayatri.e_commerce_application.domain.models.ProductDataModels
import com.gayatri.e_commerce_application.presentation.Utils.Banner
import com.gayatri.e_commerce_application.presentation.navigation.Routes
import com.gayatri.e_commerce_application.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUi(navController: NavController, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    val homeState by viewModel.homeScreenState.collectAsStateWithLifecycle()
    val getAllSuggestedProdcut = viewModel.getAllSuggestedProductsState.collectAsStateWithLifecycle()
    val getSuggestedProductData :List<ProductDataModels> = getAllSuggestedProdcut.value.userData.orEmpty().filterNotNull()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllSuggestedProducts()
    }

    if (homeState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    else if (homeState. errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = homeState.errorMessage!!)
        }
    }
    else{
        Scaffold(

        ) {innerpadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextField(
                        value ="",
                        onValueChange = {},
                        placeholder = { Text("Search")},
                        leadingIcon = { Icon(Icons. Default.Search, contentDescription = null)},
                        modifier = Modifier
                            .weight (1f)
                            .height (50. dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape (8. dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color. Transparent
                        )
                    )

                }
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Categories", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "See More", color = colorResource(id = R.color.darkGrey),
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.AllCategoriesScreen)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    LazyRow(modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16. dp)
                    ) {
                        items(homeState.categories ?: emptyList()) { category ->
                            CategoryItem(
                                ImageUri = category.categoryImage,
                                Category = category.name,
                                onClick = {
                                    navController.navigate(
                                        Routes.EachCategoryItemsScreens(
                                            categoryName = category.name
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
                homeState.banners?.let { banners ->
                    Banner(banners = banners)
                }
                //Flash Sale Section
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding (start = 16. dp, end = 16. dp, bottom = 8. dp),
                        horizontalArrangement = Arrangement. SpaceBetween
                    ){
                        Text(  "Flash Sale", style = MaterialTheme. typography. titleMedium)
                        Text (
                            "See More",
                            color = colorResource (id = R.color .darkGrey),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.SeeAllProductScreen)
                            }
                        )
                    }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16. dp),
                        horizontalArrangement = Arrangement.spacedBy (12. dp)
                    ){
                        items (homeState.products?: emptyList()) { product ->
                            ProductCard(
                                product = product,
                                navController = navController
                            )
                        }
                    }
                }
                Column (
                    modifier = Modifier. padding (top = 16.dp, bottom = 5. dp)
                ) {
                    when {
                        getAllSuggestedProdcut.value.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        getAllSuggestedProdcut.value.errorMessage != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(getAllSuggestedProdcut.value.errorMessage!!)
                            }
                        }
                        getSuggestedProductData.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No Product to suggest. Like one")
                            }
                        }
                        else -> {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding (start = 16. dp, end = 16. dp, bottom = 8. dp),
                                horizontalArrangement = Arrangement. SpaceBetween
                            ){
                                Text(  "Suggested For You", style = MaterialTheme. typography. titleMedium)
                                Text (
                                    "See More",
                                    color = colorResource (id = R.color .darkGrey),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.clickable {
                                        navController.navigate(Routes.SeeAllProductScreen)
                                    }
                                )
                            }
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16. dp),
                                horizontalArrangement = Arrangement.spacedBy (12. dp)
                            ){
                                items (getSuggestedProductData) { product ->
                                    ProductCard(
                                        product = product,
                                        navController = navController
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    ImageUri : String,
    Category: String,
    onClick : () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(end = 16.dp)
            .clickable {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.LightGray, CircleShape)
        ) {
            AsyncImage(
                model = ImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
        }
        Text (Category, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ProductCard(product: ProductDataModels, navController: NavController){
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable {
                navController.navigate(Routes.EachProductDetailsScreen(productID = product.productId))
            }
            .aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4. dp)
    ){
        Column {
            AsyncImage (
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Column (modifier = Modifier.padding (8.dp)) {
                Text(
                    text = product.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow. Ellipsis
                )
            Row(verticalAlignment = Alignment. CenterVertically) {
                Text(
                    "${product.finalPrice}/-",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "${product.price}/-",
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )


            }
            }
        }
    }
}