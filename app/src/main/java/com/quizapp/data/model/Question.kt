package com.quizapp.data.model

data class Question(
    val question: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val correctAnswer: Int // single value, 1-4
)
