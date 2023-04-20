package com.example.quizzy.features.question.data.model

import com.example.quizzy.features.question.domain.model.QuestionslistDomain

data class QuestionList(
    val response_code: Int,
    val results: List<Results>? = null
)

fun QuestionList.toQuestionsListDomain(): QuestionslistDomain {
    return QuestionslistDomain(
        response_code = response_code,
        results = results
    )
}