package com.gayatri.e_commerce_application.domain.models

data class CategoryDataModels(
    var name : String = "",
    var date : Long = System.currentTimeMillis(),
    var createBy: String = "",
    var categoryImage : String = "",
)
