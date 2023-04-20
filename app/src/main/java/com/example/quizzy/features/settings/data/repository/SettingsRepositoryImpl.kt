package com.example.quizzy.features.settings.data.repository

import com.example.quizzy.core.apiservices.ApiService
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.settings.data.model.TriviaCategory
import com.example.quizzy.features.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val apiService: ApiService) : SettingsRepository {
    override suspend fun getCategory(): Resource<List<TriviaCategory>> {
        return try {
            val response = apiService.getCategoryList()
            if (response.isSuccessful) {
                val data = response.body()?.trivia_categories ?: emptyList()
                Resource.success(data)
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Resource.error(error, null)
            }
        } catch (e: Exception) {
            Resource.error(e.toString(), null)
        }
    }
}