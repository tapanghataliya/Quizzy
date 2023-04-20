package com.example.quizzy.features.solution.data.repository

import com.example.quizzy.core.remote.ApiService
import com.example.quizzy.core.utils.Resource
import com.example.quizzy.features.question.data.model.QuestionList
import com.example.quizzy.features.question.data.model.toQuestionsListDomain
import com.example.quizzy.features.question.domain.model.QuestionslistDomain
import com.example.quizzy.features.solution.domain.repository.SolutionsRepository

class SolutionsRepositoryImpl(
    private val apiService: ApiService
): SolutionsRepository {
    override suspend fun getSolutionssList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Resource<QuestionslistDomain> {
        return try {

            val response = apiService.getQuizList(nQuestion, catID, diffType, queType)
            if (response.isSuccessful) {
                val data = response.body()?.toQuestionsListDomain()
                Resource.success(data)
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Resource.error(error, null)
            }
        }catch (e: Exception){
            Resource.error(e.toString(), null)
        }

    }

}