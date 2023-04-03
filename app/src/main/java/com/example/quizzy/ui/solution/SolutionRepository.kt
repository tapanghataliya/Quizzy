package com.example.quizzy.ui.solution

import com.example.quizzy.api.ApiClient
import com.example.quizzy.data.quiz.QuestionList
import com.example.quizzy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SolutionRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    fun getQuestionList(
        nQuestion: String,
        catID: String,
        diffType: String,
        queType: String
    ): Flow<Resource<QuestionList>> {
        return flow {
            emit(Resource.loading(null))
            val quizConfig = apiClient.getQuizList(nQuestion, catID, diffType, queType)
            emit(Resource.success(quizConfig.body()))
        }.flowOn(Dispatchers.IO)
            .catch { ex ->
                emit(Resource.error(ex.message ?: "Something went wrong", null))
            }

    }
}