package com.quizapp.ui.teacher.add_quiz.success

import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddQuizSuccessViewModel @Inject constructor() : BaseViewModel() {
    private val _quizId = MutableStateFlow<String>("")
    val quizId = _quizId.asStateFlow()

    fun setQuizId(id: String) {
        _quizId.value = id
    }
}
