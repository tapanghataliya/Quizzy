package com.example.quizzy.data.quiz

data class QuestionList(
    val response_code: Int,
    val results: List<Results>?= null
)