package com.gayatri.e_commerce_application.presentation.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gayatri.e_commerce_application.common.HomeScreenState
import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.domain.models.CartDataModels
import com.gayatri.e_commerce_application.domain.models.CategoryDataModels
import com.gayatri.e_commerce_application.domain.models.ProductDataModels
import com.gayatri.e_commerce_application.domain.models.UserData
import com.gayatri.e_commerce_application.domain.models.UserDataParent
import com.gayatri.e_commerce_application.domain.useCase.AddToFavUseCase
import com.gayatri.e_commerce_application.domain.useCase.AddtoCardUseCase
import com.gayatri.e_commerce_application.domain.useCase.CreateUserUseCase
import com.gayatri.e_commerce_application.domain.useCase.GetAllCategoryUseCase
import com.gayatri.e_commerce_application.domain.useCase.GetAllFavUseCase
import com.gayatri.e_commerce_application.domain.useCase.GetAllProductUseCase
import com.gayatri.e_commerce_application.domain.useCase.GetAllSuggestedProductsUseCase
import com.gayatri.e_commerce_application.domain.useCase.GetBannerUseCase
import com.gayatri.e_commerce_application.domain.useCase.GetCartUseCase
import com.gayatri.e_commerce_application.domain.useCase.GetCheckoutUseCase
import com.gayatri.e_commerce_application.domain.useCase.GetSpecificCategoryItems
import com.gayatri.e_commerce_application.domain.useCase.GetUserUseCase
import com.gayatri.e_commerce_application.domain.useCase.LoginUserUseCase
import com.gayatri.e_commerce_application.domain.useCase.UpDateUserDataUseCase
import com.gayatri.e_commerce_application.domain.useCase.getCategoryInLimit
import com.gayatri.e_commerce_application.domain.useCase.getProductById
import com.gayatri.e_commerce_application.domain.useCase.getproductsInLimituseCase
import com.gayatri.e_commerce_application.domain.useCase.userProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingAppViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val upDateUserDataUserCase: UpDateUserDataUseCase,
    private val userProfileImageUseCase: userProfileImageUseCase,
    private val getCategoryInLimit: getCategoryInLimit,
    private val getProductsInLimitUseCase: getproductsInLimituseCase,
    private val addtoCardUseCase: AddtoCardUseCase,
    private val geProductByID: getProductById,
    private val addtoFavUseCase: AddToFavUseCase,
    private val getAllFavUseCase: GetAllFavUseCase,
    private val getAllCategoriesUseCase: GetAllCategoryUseCase,
    private val getCheckoutUseCase: GetCheckoutUseCase,
    private val getBannerUseCase: GetBannerUseCase,
    private val getSpecificCategoryItems: GetSpecificCategoryItems,
    private val getAllSuggestedProductsUseCase: GetAllSuggestedProductsUseCase,
    private val getAllProductsUseCase: GetAllProductUseCase,
    private val getCartUseCase: GetCartUseCase
) :
    ViewModel()
{
    private val _singUpScreenState = MutableStateFlow(SignUpScreenState())
    val singUpScreenState = _singUpScreenState.asStateFlow()
    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()
    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState = _profileScreenState.asStateFlow()
    private val _upDateScreenState = MutableStateFlow(UpDateScreenState())
    val upDateScreenState = _upDateScreenState.asStateFlow()
    private val _userProfileImageState = MutableStateFlow(uploadUserProfileImageState())
    val userProfileImageState = _userProfileImageState.asStateFlow()
    private val _addToCartState = MutableStateFlow(AddtoCartState())
    val addToCartState = _addToCartState.asStateFlow()
    private val _getProductByIDState = MutableStateFlow(GetProductByIDState())
    val getProductByIDState = _getProductByIDState.asStateFlow()
    private val _addToFavState = MutableStateFlow(AddToFavState())
    val addToFavState = _addToFavState.asStateFlow()
    private val _getAllFavState = MutableStateFlow(GetAllFavState())
    val getAllFavState = _getAllFavState.asStateFlow()
    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState = _getAllProductsState.asStateFlow()
    private val _getCartState = MutableStateFlow(GetCartState())
    val getCartState = _getCartState.asStateFlow()
    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState = _getAllCategoriesState.asStateFlow()
    private val _getCheckoutState = MutableStateFlow(GetCheckoutState())
    val getCheckoutState = _getCheckoutState.asStateFlow()
    private val _getSpecifiCategoryItemsState = MutableStateFlow(GetSpecificCategoryItemsState())
    val getSpecifiCategoryItemsState = _getSpecifiCategoryItemsState.asStateFlow()
    private val _getAllSuggestedProductsState = MutableStateFlow(GetAllSuggestedProductsState())
    val getAllSuggestedProductsState = _getAllSuggestedProductsState.asStateFlow()
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()


    fun getSpecifiCategoryItems(categoryName : String) {
        viewModelScope.launch {
            getSpecificCategoryItems.getSpecificCategoryItems(categoryName).collect {
                when (it) {
                    is ResultState.Error -> {
                        _getSpecifiCategoryItemsState.value =
                            _getSpecifiCategoryItemsState.value.copy(
                                isLoading = false,
                                errorMessage = it.message
                            )
                    }

                    is ResultState.Loading -> {
                        _getSpecifiCategoryItemsState.value =
                            _getSpecifiCategoryItemsState.value.copy(
                                isLoading = true
                            )
                    }
                    is ResultState.Success -> {
                        _getSpecifiCategoryItemsState.value =
                            _getSpecifiCategoryItemsState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                    }
                }
            }

        }

    }

    fun getCheckOut(productId: String) {
        viewModelScope.launch {
            getCheckoutUseCase.getCheckoutUseCse(productId).collect {
                when (it) {
                    is ResultState.Error -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase.getAllCategoriesUseCase().collect {
                when (it) {
                    is ResultState.Error -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getCart() {
        viewModelScope.launch {
            getCartUseCase.getCart().collect {
                when(it) {
                    is ResultState.Error -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState. Loading -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState. Success -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllProducts(){
        viewModelScope. launch{
            getAllProductsUseCase.getAllProduct().collect {
                when (it) {
                    is ResultState.Error -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllFav() {
        viewModelScope.launch {
            getAllFavUseCase.getAllFav().collect {
                when (it) {
                    is ResultState.Error -> {
                        _getAllFavState.value = _getAllFavState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState. Loading->{
                        _getAllFavState.value = _getAllFavState.value. copy(
                            isLoading = true
                        )
                    }
                    is ResultState. Success->{
                        _getAllFavState. value = _getAllFavState.value. copy(
                            isLoading = false,
                            userData = it. data
                        )
                    }
                }
            }
        }
    }

    fun addToFav(productDataModels: ProductDataModels) {
        viewModelScope.launch {
            addtoFavUseCase.addtoFav(productDataModels).collect{
                when (it) {
                    is ResultState.Error -> {
                        _addToFavState.value = _addToFavState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _addToFavState.value = _addToFavState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState. Success -> {
                        _addToFavState. value = _addToFavState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getProductByID(productId : String){
        viewModelScope. launch {
            geProductByID. getProductById(productId).collect {
                when (it) {
                    is ResultState.Error -> {
                        _getProductByIDState.value = _getProductByIDState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getProductByIDState.value = _getProductByIDState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState. Success ->{
                        _getProductByIDState. value = _getProductByIDState.value.copy (
                            isLoading = false,
                            userData = it. data
                        )
                    }
                }
            }
        }
    }

    fun addToCart(cartDataModels: CartDataModels) {
        viewModelScope. launch {
            addtoCardUseCase.addtoCard(cartDataModels).collect{
                when (it){
                    is ResultState. Error -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState.Loading -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState. Success -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    init {
        LoadHomeScreenData()
    }

    fun LoadHomeScreenData(){
        viewModelScope. launch {
            combine(
                getCategoryInLimit.getCategoryInLimited(),
                getProductsInLimitUseCase.getProductInLimit(),
                getBannerUseCase.getBannerUseCase()
            ) { categoriesResult, productsResult, bannerResult ->
                when {
                    categoriesResult is ResultState.Error -> {
                        HomeScreenState(isLoading = false, errorMessage = categoriesResult.message)
                    }
                    productsResult is ResultState. Error ->{
                        HomeScreenState(isLoading = false, errorMessage = productsResult.message)
                    }
                    bannerResult is ResultState. Error-> {
                        HomeScreenState(isLoading = false, errorMessage = bannerResult.message)
                    }
                    categoriesResult is ResultState.Success && productsResult is ResultState. Success && bannerResult is ResultState. Success ->{
                        HomeScreenState(
                            isLoading = false,
                            categories = categoriesResult.data,
                            products = productsResult.data,
                            banners = bannerResult.data
                        )
                    }
                    else -> {
                        HomeScreenState(isLoading = true)
                    }
                }
            }.collect{
                state -> _homeScreenState.value = state
            }
        }
    }

    fun upLoadUserProfileImage(uri: Uri){
        viewModelScope. launch {
            userProfileImageUseCase.userProfileImage(uri).collect {
                when (it) {
                    is ResultState.Error -> {
                        _userProfileImageState.value = _userProfileImageState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _userProfileImageState.value = _userProfileImageState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _userProfileImageState.value = _userProfileImageState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun upDateUserData(userDataParent: UserDataParent) {
        viewModelScope.launch {
            upDateUserDataUserCase.upDateUserData(userDataParent = userDataParent).collect {
                when (it) {
                    is ResultState.Error -> {
                        _upDateScreenState.value = _upDateScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState.Loading -> {
                        _upDateScreenState.value = _upDateScreenState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState. Success -> {
                        _upDateScreenState.value = _upDateScreenState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun createUser(userData: UserData) {
        viewModelScope.launch {
            createUserUseCase.createUser(userData).collect {
                when (it) {
                    is ResultState.Error -> {
                        _singUpScreenState.value = _singUpScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState. Loading -> {
                        _singUpScreenState. value = _singUpScreenState.value.copy (
                                isLoading = true
                        )
                    }
                    is ResultState.Success -> {
                        _singUpScreenState.value = _singUpScreenState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun loginUser(userData: UserData) {
        viewModelScope.launch {
            loginUserUseCase.loginUser(userData).collect {
                when (it) {
                    is ResultState.Error -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState. Loading -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState. Success -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getUserById(uid: String) {
        viewModelScope.launch {
            getUserUseCase.getUserById(uid).collect {
                when (it) {
                    is ResultState.Error -> {
                        _profileScreenState.value = _profileScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _profileScreenState.value = _profileScreenState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success ->{
                        _profileScreenState.value = _profileScreenState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllSuggestedProducts () {
        viewModelScope.launch {
            getAllSuggestedProductsUseCase.getAllSuggestedProducts().collect {
                when (it) {
                    is ResultState.Error -> {
                        _getAllSuggestedProductsState.value = _getAllSuggestedProductsState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState.Loading -> {
                        _getAllSuggestedProductsState.value = _getAllSuggestedProductsState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success -> {
                        _getAllSuggestedProductsState.value = _getAllSuggestedProductsState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

}



data class ProfileScreenState(
    val isLoading :Boolean = false,
    val errorMessage: String? = null,
    val userData : UserDataParent? = null
)
data class SignUpScreenState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : String? = null
)
data class LoginScreenState(
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val userData : String? = null
)
data class UpDateScreenState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : String? = null
)
data class uploadUserProfileImageState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : String? = null
)
data class AddtoCartState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : String? = null
)
data class GetProductByIDState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : ProductDataModels? = null
)
data class AddToFavState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : String? = null
)
data class GetAllFavState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : List<ProductDataModels?> = emptyList()
)
data class GetAllProductsState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : List<ProductDataModels?> = emptyList()
)
data class GetCartState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : List<CartDataModels?> = emptyList()
)
data class GetAllCategoriesState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : List<CategoryDataModels?> = emptyList()
)
data class GetCheckoutState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : ProductDataModels? = null
)
data class GetSpecificCategoryItemsState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : List<ProductDataModels?> = emptyList()
)
data class GetAllSuggestedProductsState(
    val isLoading :Boolean = false,
    val errorMessage : String? = null,
    val userData : List<ProductDataModels?> = emptyList()
)