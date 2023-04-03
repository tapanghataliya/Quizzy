package com.example.quizzy.data.quiz

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Results(
    val category: String?,
    val correct_answer: String?,
    val difficulty: String?,
    val incorrect_answers: List<String>?,
    val question: String?,
    val type: String?
): Parcelable