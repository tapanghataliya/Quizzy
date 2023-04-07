package com.example.quizzy.features.question.domain

import com.example.quizzy.core.apiservices.ApiHelper
import com.example.quizzy.features.question.data.QuestionList
import retrofit2.Response
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getQuestionList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Response<QuestionList> = apiHelper.getQuestionList(nQuestion, catID, diffType, queType)

}

