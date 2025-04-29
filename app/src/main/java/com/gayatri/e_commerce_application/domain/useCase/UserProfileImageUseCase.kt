package com.gayatri.e_commerce_application.domain.useCase

import android.net.Uri
import com.gayatri.e_commerce_application.common.ResultState
import com.gayatri.e_commerce_application.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class userProfileImageUseCase @Inject constructor (private val repo: Repo) {
    fun userProfileImage(uri: Uri): Flow<ResultState<String>> {
        return repo.userProfileImage(uri)
    }
}