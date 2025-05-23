package com.quizapp.ui.teacher.add_quiz

import com.quizapp.data.model.Quiz
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddQuizViewModel  @Inject constructor(
) : BaseViewModel() {
    private val _quizTitle = MutableStateFlow<String>("")
    val quizTitle = _quizTitle.asStateFlow()

    private val _quizDesc = MutableStateFlow<String>("")
    val quizDesc = _quizDesc.asStateFlow()

    fun setQuizTitle(title: String) {
        _quizTitle.value = title
    }

    fun setQuizDesc(desc: String) {
        _quizDesc.value = desc
    }
}