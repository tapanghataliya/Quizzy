package com.example.quizzy.core.remote

import com.example.quizzy.features.settings.data.model.CategoryListResponse
import com.example.quizzy.features.question.data.model.QuestionList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("api_category.php")
    suspend fun getCategoryList(): Response<CategoryListResponse>

    //   amount=10&category=10&difficulty=easy&type=multiple
        @GET("api.php?amount=10")
        suspend fun getQuizList(
            @Query(value = "amount", encoded = true) nQuestion: String,
            @Query(value = "category", encoded = true) catID: String,
            @Query(value = "difficulty", encoded = true) diffType: String,
            @Query(value = "type", encoded = true) queType: String
        ): Response<QuestionList>

}