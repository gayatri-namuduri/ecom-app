package com.gayatri.e_commerce_application.domain.useCase

import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.domain.models.ProductDataModels
import com.gayatri.e_commerce_application.domain.repo.Repo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllFavUseCase @Inject constructor (private val repo: Repo) {
    fun getAllFav(): Flow<ResultState<List<ProductDataModels>>> {
        return repo.getallFav()
    }
}