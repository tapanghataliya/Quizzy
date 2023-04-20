package com.example.quizzy.features.question.data.model

import android.os.Parcelable
import com.example.quizzy.features.question.domain.model.ResultsDomain
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

fun Results.toResulstDomain(): ResultsDomain {
    return ResultsDomain(
        category = category,
        correct_answer = correct_answer,
        difficulty = difficulty,
        incorrect_answers = incorrect_answers,
        question = question,
        type = type
    )
}