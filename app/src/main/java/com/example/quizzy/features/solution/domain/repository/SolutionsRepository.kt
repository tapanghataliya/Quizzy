package com.example.quizzy.features.solution.domain.repository

import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.data.model.QuestionList
import com.example.quizzy.features.question.domain.model.QuestionslistDomain

interface SolutionsRepository {
    suspend fun getSolutionssList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Resource<QuestionslistDomain>
}