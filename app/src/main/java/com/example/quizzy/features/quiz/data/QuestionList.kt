package com.example.quizzy.features.quiz.data

data class QuestionList(
    val response_code: Int,
    val results: List<Results>?= null
)