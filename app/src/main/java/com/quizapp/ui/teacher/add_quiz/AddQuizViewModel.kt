package com.quizapp.ui.teacher.add_quiz

import com.quizapp.data.model.Quiz
import com.quizapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddQuizViewModel: BaseViewModel() {
    private val _quizTitle = MutableStateFlow<String>("")
    val quizTitle: StateFlow<String> = _quizTitle

    private val _quizDesc = MutableStateFlow<String>("")
    val quizDesc: StateFlow<String> = _quizDesc

    fun setQuizTitle(title: String) {
        _quizTitle.value = title
    }

    fun setQuizDesc(desc: String) {
        _quizDesc.value = desc
    }
}