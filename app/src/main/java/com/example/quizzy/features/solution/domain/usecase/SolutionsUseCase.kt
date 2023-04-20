package com.example.quizzy.features.solution.domain.usecase

import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.data.model.QuestionList

interface SolutionsUseCase {
    suspend fun solutionsExecute(nQuestion: String,
                                 catID: String,
                                 diffType: String,
                                 queType: String): Resource<QuestionList>
}