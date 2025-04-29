package com.gayatri.e_commerce_application.common

import com.gayatri.e_commerce_application.domain.models.BannerDataModels
import com.gayatri.e_commerce_application.domain.models.CategoryDataModels
import com.gayatri.e_commerce_application.domain.models.ProductDataModels

data class HomeScreenState (
    val isLoading: Boolean = true,
    val errorMessage : String? = null,
    val categories : List<CategoryDataModels>? = null,
    val products : List<ProductDataModels>? = null,
    val banners : List<BannerDataModels>? = null
)