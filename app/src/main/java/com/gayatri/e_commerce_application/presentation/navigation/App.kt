package com.gayatri.e_commerce_application.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.gayatri.e_commerce_application.R
import com.gayatri.e_commerce_application.presentation.LoginScreenUi
import com.gayatri.e_commerce_application.presentation.Screens.AllCategoriesScreen
import com.gayatri.e_commerce_application.presentation.Screens.CartScreen
import com.gayatri.e_commerce_application.presentation.Screens.CheckOutScreen
import com.gayatri.e_commerce_application.presentation.Screens.EachCategoryProductScreenUi
import com.gayatri.e_commerce_application.presentation.Screens.EachProductDetailsScreenUi
import com.gayatri.e_commerce_application.presentation.Screens.GetAllProducts
import com.gayatri.e_commerce_application.presentation.Screens.HomeScreenUi
import com.gayatri.e_commerce_application.presentation.Screens.ProfileScreenUi
import com.gayatri.e_commerce_application.presentation.Screens.getAllFav
import com.gayatri.e_commerce_application.presentation.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

data class BottomNavItem(val name:String, val icon: ImageVector, val unselectedIcon : ImageVector)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App (
    firebaseAuth: FirebaseAuth,
    payTest:() -> Unit
) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = remember { mutableStateOf(false) }
    LaunchedEffect(currentDestination) {
        shouldShowBottomBar.value = when (currentDestination) {
            Routes.LoginScreen::class.qualifiedName, Routes.SingUpScreen::class.qualifiedName -> false
            else -> true
        }
    }
    val BottomNavItem = listOf(
        BottomNavItem("Home", Icons.Default.Home, unselectedIcon = Icons.Outlined.Home),
        BottomNavItem("WishList", Icons.Default.Favorite, unselectedIcon = Icons.Outlined.Favorite),
        BottomNavItem(
            "Cart",
            Icons.Default.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart
        ),
        BottomNavItem("Profile", Icons.Default.Person, unselectedIcon = Icons.Outlined.Person)
    )
    var startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.LoginSingUpScreen
    } else {
        SubNavigation.MainHomeScreen
    }


    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar.value) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                        )

                ) {
                    AnimatedBottomBar(
                        selectedItem = selectedItem,
                        itemSize = BottomNavItem.size,
                        containerColor = Color.Transparent,
                        indicatorColor = colorResource(id = R.color.darkGrey),
                        indicatorDirection = IndicatorDirection.BOTTOM,
                        indicatorStyle = IndicatorStyle.FILLED
                    ) {
                        BottomNavItem.forEachIndexed { index, navigationItem ->
                            BottomBarItem(
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    when (index) {
                                        0 -> navController.navigate(Routes.HomeScreen)
                                        1 -> navController.navigate(Routes.WishListScreen)
                                        2 -> navController.navigate(Routes.CartScreen)
                                        3 -> navController.navigate(Routes.ProfileScreen)
                                    }
                                },
                                imageVector = navigationItem.icon,
                                label = navigationItem.name,
                                containerColor = Color.Transparent
                            )
                        }
                    }
                }
            }

        }) {  innerpadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding (bottom = if (shouldShowBottomBar. value) 60.dp else 0.dp)
        ) {
            NavHost(navController = navController, startDestination = startScreen) {
                navigation<SubNavigation.LoginSingUpScreen>(startDestination = Routes.LoginScreen) {
                    composable<Routes.LoginScreen> {
                        LoginScreenUi(navController = navController)
                    }
                    composable<Routes. SingUpScreen> {
                        SignUpScreen(navController = navController)
                    }
                }
                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes. HomeScreen){
                    composable<Routes. HomeScreen> {
                        HomeScreenUi(navController = navController)
                    }
                    composable<Routes. ProfileScreen> {
                        ProfileScreenUi(navController = navController, firebaseAuth = firebaseAuth)
                    }
                    composable<Routes.WishListScreen> {
                        getAllFav(navController = navController)
                    }
                    composable<Routes. CartScreen> {
                        CartScreen (navController = navController)
                    }
                    composable<Routes. SeeAllProductScreen> {
                        GetAllProducts(navController = navController)
                    }
                    composable<Routes.AllCategoriesScreen> {
                        AllCategoriesScreen(navController = navController)
                    }
                }
                composable<Routes.EachProductDetailsScreen> {
                    val product : Routes.EachProductDetailsScreen= it.toRoute()
                    EachProductDetailsScreenUi(navController = navController, productId = product.productID)
                }
                composable<Routes.EachCategoryItemsScreens> {
                    val category: Routes.EachCategoryItemsScreens = it.toRoute()
                    EachCategoryProductScreenUi(
                        navController = navController,
                        categoryName = category.categoryName
                    )
                }
                composable<Routes.CheckoutScreen> {
                    val product: Routes. EachProductDetailsScreen = it. toRoute()
                    CheckOutScreen (productID = product.productID, navController = navController, pay = payTest)
                }

            }
        }
    }
}