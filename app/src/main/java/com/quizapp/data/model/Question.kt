package com.quizapp.data.model

data class Question(
    val id: String ?= null,
    var text: String = "",
    var type: QuestionType = QuestionType.MULTIPLE_CHOICE,
    var options: List<Option> = emptyList()
)