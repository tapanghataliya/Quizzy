package com.example.quizzy.features.settings.data

data class MySettingsData(
    val nQuestion: String,
    val categoryID: String,
    val difficultyType: String,
    val questionsType: String,
    val isCheck: Boolean
)
