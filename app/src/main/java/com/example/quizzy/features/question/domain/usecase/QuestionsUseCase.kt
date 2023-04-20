package com.example.quizzy.features.question.domain.usecase

import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.data.model.QuestionList
import com.example.quizzy.features.question.domain.model.QuestionslistDomain

interface QuestionsUseCase {
    suspend fun questionsExecute(nQuestion: String,
                                 catID: String,
                                 diffType: String,
                                 queType: String): Resource<QuestionslistDomain>
}