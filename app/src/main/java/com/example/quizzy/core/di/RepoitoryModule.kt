package com.example.quizzy.core.di

import android.app.Application
import android.content.Context
import com.example.quizzy.core.remote.ApiService
import com.example.quizzy.features.question.data.repository.QuestionsRepositoryImpl
import com.example.quizzy.features.question.domain.repository.QuestionsRepository
import com.example.quizzy.features.settings.data.repository.SettingsRepositoryImpl
import com.example.quizzy.features.settings.domain.repository.SettingsRepository
import com.example.quizzy.features.solution.data.repository.SolutionsRepositoryImpl
import com.example.quizzy.features.solution.domain.repository.SolutionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoitoryModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideSettingRepository(apiService: ApiService,context: Context): SettingsRepository {
        return SettingsRepositoryImpl(apiService, context)
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