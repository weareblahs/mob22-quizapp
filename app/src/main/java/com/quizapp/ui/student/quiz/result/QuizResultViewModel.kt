package com.quizapp.ui.student.quiz.result


import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.QuizHistory
import com.quizapp.data.repo.StudentRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(private val authService: AuthService, private val repo: StudentRepo) : BaseViewModel() {
    private val _successfulSave = MutableStateFlow<Boolean>(false)
    val successfulSave = _successfulSave.asStateFlow()

    fun saveQuiz(history: QuizHistory) {
        viewModelScope.launch {
            errorHandler {
                repo.storeHistory(history)
                _successfulSave.update {true}
            }
        }
    }
}
