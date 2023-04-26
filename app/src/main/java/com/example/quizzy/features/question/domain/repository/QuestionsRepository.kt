package com.example.quizzy.features.question.domain.repository

import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.domain.model.QuestionslistDomain

interface QuestionsRepository {
    suspend fun getQuestionsList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Resource<QuestionslistDomain>
}