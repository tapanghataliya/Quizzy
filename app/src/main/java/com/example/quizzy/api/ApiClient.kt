package com.example.quizzy.api

import com.example.quizzy.data.Category.CategoryListResponse
import com.example.quizzy.data.quiz.QuestionList
import com.example.quizzy.data.quiz.QuizResponse
import com.example.quizzy.utils.Resource
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

    suspend fun getCategoryList(): Response<CategoryListResponse> {
        return apiService.getCategoryList()
    }

    suspend fun getQuizList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Response<QuestionList> {
        return apiService.getQuizList(nQuestion,catID, diffType, queType)
    }
}