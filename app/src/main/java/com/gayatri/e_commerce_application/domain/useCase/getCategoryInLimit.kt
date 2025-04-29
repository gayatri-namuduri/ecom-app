package com.gayatri.e_commerce_application.domain.useCase

import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.domain.models.CategoryDataModels
import com.gayatri.e_commerce_application.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class getCategoryInLimit @Inject constructor (private val repo: Repo) {
    fun getCategoryInLimited(): Flow<ResultState<List<CategoryDataModels>>> {
        return repo.getCategoriesInLimited()
    }
}