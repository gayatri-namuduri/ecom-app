package com.gayatri.e_commerce_application.domain.useCase

import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.domain.models.UserDataParent
import com.gayatri.e_commerce_application.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repo: Repo) {
    fun getUserById(uid: String): Flow<ResultState<UserDataParent>> {
        return repo.getuserById(uid)
    }
}