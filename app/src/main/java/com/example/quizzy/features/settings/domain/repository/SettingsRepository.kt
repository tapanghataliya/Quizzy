package com.example.quizzy.features.settings.domain.repository

import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.settings.data.model.TriviaCategory

interface SettingsRepository {
    suspend fun getCategory(): Resource<List<TriviaCategory>>
}