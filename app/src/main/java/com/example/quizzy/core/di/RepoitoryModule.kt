package com.example.quizzy.core.di

import com.example.quizzy.core.apiservices.ApiService
import com.example.quizzy.features.question.data.repository.QuestionsRepositoryImpl
import com.example.quizzy.features.question.domain.repository.QuestionsRepository
import com.example.quizzy.features.settings.data.repository.SettingsRepositoryImpl
import com.example.quizzy.features.settings.domain.repository.SettingsRepository
import com.example.quizzy.features.solution.data.SolutionsRepositoryImpl
import com.example.quizzy.features.solution.domain.repository.SolutionsRepository
import com.example.quizzy.features.solution.domain.usecase.SolutionsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepoitoryModule {

    @Provides
    fun provideSettingRepository(apiService: ApiService): SettingsRepository {
        return SettingsRepositoryImpl(apiService)
    }

    @Provides
    fun provideQuestionsRepository(apiService: ApiService): QuestionsRepository {
        return QuestionsRepositoryImpl(apiService)
    }

    @Provides
    fun provideSolutionsRepository(apiService: ApiService): SolutionsRepository{
        return SolutionsRepositoryImpl(apiService)
    }
}