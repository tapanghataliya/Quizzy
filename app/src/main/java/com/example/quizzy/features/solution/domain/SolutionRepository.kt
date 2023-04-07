package com.example.quizzy.features.solution.domain

import com.example.quizzy.core.apiservices.ApiHelper
import com.example.quizzy.features.question.data.QuestionList
import retrofit2.Response
import javax.inject.Inject

class SolutionRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getSolutionList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Response<QuestionList> = apiHelper.getQuestionList(nQuestion, catID, diffType, queType)
}