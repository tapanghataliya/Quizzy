package com.example.quizzy.features.question.data.repository

import com.example.quizzy.core.apiservices.ApiService
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.data.model.QuestionList
import com.example.quizzy.features.question.domain.repository.QuestionsRepository

class QuestionsRepositoryImpl(
    private val apiService: ApiService
): QuestionsRepository {
    override suspend fun getQuestionsList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Resource<QuestionList> {
        try {

            val response = apiService.getQuizList(nQuestion, catID, diffType, queType)
            if (response.isSuccessful) {
//                val data = response.body()?.results?: emptyList()
                val data = response.body()
                return Resource.success(data)
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                return Resource.error(error, null)
            }
        }catch (e: Exception){
            return Resource.error(e.toString(), null)
        }
    }
}