package com.example.quizzy.features.settings.domain.usecase

import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.settings.data.model.TriviaCategory

interface SettingsUseCase {
    suspend fun categoryExecute(): Resource<List<TriviaCategory>>
}