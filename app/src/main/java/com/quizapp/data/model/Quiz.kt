package com.quizapp.data.model

data class Quiz(
    val id: String ?= null,
    var title: String = "",
    var description: String = "",
    var questions: List<Question> = emptyList(),
    var createdBy: String = "",
)
