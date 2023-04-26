package com.example.quizzy.features.settings.data.repository

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.quizzy.core.remote.ApiService
import com.example.quizzy.core.utils.Constant
import com.example.quizzy.core.utils.NetworkUtils
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.settings.data.model.TriviaCategory
import com.example.quizzy.features.settings.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class SettingsRepositoryImpl(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : SettingsRepository {
    @RequiresApi(Build.VERSION_CODES.M)
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