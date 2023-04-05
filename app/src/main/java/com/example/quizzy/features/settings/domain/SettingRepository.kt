package com.example.quizzy.features.settings.domain

import com.example.quizzy.core.apiservices.ApiHelper
import com.example.quizzy.features.settings.data.CategoryListResponse
import retrofit2.Response
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getCategoryList(): Response<CategoryListResponse> = apiHelper.getCategoryList()
}
