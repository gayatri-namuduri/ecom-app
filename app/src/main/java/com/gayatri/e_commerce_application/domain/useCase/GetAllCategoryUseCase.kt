package com.gayatri.e_commerce_application.domain.useCase

import kotlinx.coroutines.flow.Flow
import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.domain.models.CategoryDataModels
import com.gayatri.e_commerce_application.domain.repo.Repo
import javax.inject.Inject

class GetAllCategoryUseCase @Inject constructor(private val repo: Repo) {
    fun getAllCategoriesUseCase(): Flow<ResultState<List<CategoryDataModels>>> {
        return repo.getAllCategories()
    }
}