package com.example.quizzy.core.di

import com.example.quizzy.features.question.domain.repository.QuestionsRepository
import com.example.quizzy.features.question.domain.usecase.QuestionsUseCase
import com.example.quizzy.features.question.domain.usecase.QuestionsUseCaseImpl
import com.example.quizzy.features.settings.domain.repository.SettingsRepository
import com.example.quizzy.features.settings.domain.usecase.SettingsUseCase
import com.example.quizzy.features.settings.domain.usecase.SettingsUseCaseImpl
import com.example.quizzy.features.solution.domain.repository.SolutionsRepository
import com.example.quizzy.features.solution.domain.usecase.SolutionsUseCase
import com.example.quizzy.features.solution.domain.usecase.SolutionsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideSettingUseCase(settingsRespository: SettingsRepository): SettingsUseCase {
        return SettingsUseCaseImpl(settingsRespository)
    }

    @Provides
    fun provideQuestionsUseCase(questionsRepository: QuestionsRepository): QuestionsUseCase {
        return QuestionsUseCaseImpl(questionsRepository)
    }

    @Provides
    fun prodiveSolutionUseCase(solutionsRepository: SolutionsRepository): SolutionsUseCase{
        return SolutionsUseCaseImpl(solutionsRepository)
    }
}