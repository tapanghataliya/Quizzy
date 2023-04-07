package com.example.quizzy.features.quiz.domain

import android.content.Context
import com.example.quizzy.core.apiservices.ApiHelper
import com.example.quizzy.features.quiz.data.QuestionList
import dagger.hilt.android.qualifiers.ApplicationContext
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

