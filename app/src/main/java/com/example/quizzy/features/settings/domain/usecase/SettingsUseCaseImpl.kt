package com.example.quizzy.features.settings.domain.usecase

import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.settings.data.model.TriviaCategory
import com.example.quizzy.features.settings.domain.repository.SettingsRepository

class SettingsUseCaseImpl(private val settingsRepository: SettingsRepository):
    SettingsUseCase {
    override suspend fun categoryExecute(): Resource<List<TriviaCategory>> {
        return settingsRepository.getCategory()
    }
}