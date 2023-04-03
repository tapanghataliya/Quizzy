package com.example.quizzy.ui.settings

import com.example.quizzy.api.ApiClient
import com.example.quizzy.data.Category.CategoryListResponse
import com.example.quizzy.data.Category.TriviaCategory
import com.example.quizzy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    fun getCetegory():Flow<Resource<CategoryListResponse>>{

        return flow {
            emit(Resource.loading(null))
            val categoryConfig =apiClient.getCategoryList()
            emit(Resource.success(categoryConfig.body()))
        }.flowOn(Dispatchers.IO)
            .catch { ex ->
                emit(Resource.error(ex.message?: "Something went wrong", null))
            }
    }

}