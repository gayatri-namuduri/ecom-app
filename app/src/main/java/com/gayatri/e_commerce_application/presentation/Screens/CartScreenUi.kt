package com.gayatri.e_commerce_application.presentation.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gayatri.e_commerce_application.R
import com.gayatri.e_commerce_application.domain.models.CartDataModels
import com.gayatri.e_commerce_application.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen( navController: NavController, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    val cartState = viewModel.getCartState.collectAsStateWithLifecycle()
    val cartData = cartState.value.userData?: emptyList()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior (rememberTopAppBarState())

    LaunchedEffect(key1 = Unit) {
        viewModel.getCart()
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "Shopping Cart",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
        ){
            when {
                cartState.value.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                cartState.value.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Spacer (modifier = Modifier.padding(8.dp))
                        Text("Sorry, Unable to get Information")
                    }
                }

                cartData.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text( "No Products Available")
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ){
                        Row(
                            modifier = Modifier.padding(8.dp)
                        ){
                            Text(
                                text = "Items",
                                style = MaterialTheme. typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(.45f))
                            Text(
                                text = "Details",
                                style = MaterialTheme. typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "QTY",
                                style = MaterialTheme. typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(.15f))
                        }
                        LazyColumn (
                            modifier = Modifier.weight(.6f)
                        ){
                            items(cartData) {item ->
                                CartItemCard(item = item!!)
                            }
                        }
                        HorizontalDivider (modifier = Modifier.padding(top = 16.dp, bottom = 8. dp))
                        Button (
                            onClick = { /* handle the checkout */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp),
                            shape = RoundedCornerShape (8. dp),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.darkGrey))
                        ){
                            Text("Checkout")
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CartItemCard(item: CartDataModels) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme. typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Size:${item.size}",
                    style = MaterialTheme. typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Price:${item.price}",
                    style = MaterialTheme. typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Column (
                    horizontalAlignment = Alignment. End
            ) {
                Text(
                    text = "QTY: ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}