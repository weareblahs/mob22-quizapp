package com.quizapp.ui.student.startquiz

import androidx.lifecycle.viewModelScope
import com.quizapp.data.repo.StudentRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartQuizViewModel @Inject constructor(private val repo: StudentRepo) : BaseViewModel() {
    private val _quizInfo = MutableStateFlow(QuizInfo())
    val quizInfo = _quizInfo.asStateFlow()

    fun loadQuiz(code: String) {
        viewModelScope.launch {
            val quiz = repo.getQuiz(code)
            if (quiz != null) {
                _quizInfo.update {it.copy(quizLoaded = true, quizName = quiz.title, quizDescription = quiz.description)}
            }
        }
    }
}

data class QuizInfo (
    val quizName: String = "",
    val quizDescription: String = "",
    val quizLoaded: Boolean = true
)
