package com.example.quizzy.features.question.domain.model

import com.example.quizzy.features.question.data.model.Results

data class QuestionslistDomain(
    val response_code: Int,
    val results: List<Results>? = null

)
