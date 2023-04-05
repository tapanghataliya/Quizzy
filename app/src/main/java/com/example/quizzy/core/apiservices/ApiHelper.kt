package com.example.quizzy.core.apiservices

import com.example.quizzy.features.settings.data.CategoryListResponse
import com.example.quizzy.features.quiz.data.QuestionList
import retrofit2.Response

interface ApiHelper {
    suspend fun getQuestionList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Response<QuestionList>

    suspend fun getCategoryList(): Response<CategoryListResponse>
}