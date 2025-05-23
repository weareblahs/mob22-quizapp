package com.quizapp.data.model

data class Question(
    val id: String ?= null,
    var text: String = "",
    var type: QuestionType = QuestionType.SINGLE_CHOICE,
    var answer: Int = 1,
    var options: List<Option> = emptyList()
)