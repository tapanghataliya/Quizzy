package com.example.quizzy.features.settings.data.model

data class MySettingsData(
    val nQuestion: String,
    val categoryID: String,
    val categoryName: String,
    val difficultyType: String,
    val questionsType: String,
    val isCheck: Boolean
)
