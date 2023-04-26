package com.example.quizzy.features.question.domain.usecase

import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.domain.model.QuestionslistDomain
import com.example.quizzy.features.question.domain.repository.QuestionsRepository

class QuestionsUseCaseImpl(
    private val questionsRepository: QuestionsRepository
) : QuestionsUseCase {

    override suspend fun questionsExecute(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Resource<QuestionslistDomain> {
        return questionsRepository.getQuestionsList(
            nQuestion,
            catID,
            diffType,
            queType
        )
    }
}