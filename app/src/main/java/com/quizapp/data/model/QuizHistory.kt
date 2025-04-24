package com.quizapp.data.model

data class QuizHistory (
    val id: String? = null,
    val code: String? = "",
    val correctQuestions: Int? = -1,
    val wrongQuestions: Int? = -1,
    val points: Int? = -1,
    val quizName: String? = "",
    val doneDate: Long? = System.currentTimeMillis() // usually this does not have to be recorded because it is automatically recorded when a quiz is done
)