package com.quizapp.ui.student.quiz

import androidx.lifecycle.viewModelScope
import com.quizapp.data.model.Question
import com.quizapp.data.repo.StudentRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakeQuizViewModel @Inject constructor(private val repo: StudentRepo) : BaseViewModel() {
    private val _question = MutableStateFlow<Question>(Question())
    val question = _question.asStateFlow()

    fun retrieveQuestion(quizId: String, question: Int) {
        viewModelScope.launch {
            val questionList = repo.getQuiz(quizId)?.questions
            _question.update {
                questionList!![question - 1]
            }
        }
    }
}
