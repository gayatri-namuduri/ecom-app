package com.gayatri.e_commerce_application.domain.useCase

import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.domain.models.UserData
import com.gayatri.e_commerce_application.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(private val repo: Repo) {
    fun loginUser(userData: UserData): Flow<ResultState<String>> {
        return repo.loginUserWithEmailAndPassword(userData)
    }
}