package com.example.quizzy.core.apiservices

import com.example.quizzy.features.settings.data.CategoryListResponse
import com.example.quizzy.features.question.data.QuestionList
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getQuestionList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Response<QuestionList> = apiService.getQuizList(nQuestion, catID, diffType, queType)

    override suspend fun getCategoryList(): Response<CategoryListResponse> =
        apiService.getCategoryList()
}