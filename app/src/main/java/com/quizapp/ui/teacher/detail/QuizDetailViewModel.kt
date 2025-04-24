package com.quizapp.ui.teacher.detail

import com.quizapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizDetailViewModel: BaseViewModel() {
    private val _quizId = MutableStateFlow<String>("")
    val quizId =  _quizId.asStateFlow()

    fun setQuizId(id: String) {
        _quizId.value = id
    }
}