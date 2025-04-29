package com.gayatri.e_commerce_application.domain.useCase

import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.domain.models.ProductDataModels
import com.gayatri.e_commerce_application.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductUseCase @Inject constructor(private val repo: Repo) {
    fun getAllProduct(): Flow<ResultState<List<ProductDataModels>>> {
        return repo.getAllProducts()
    }
}
