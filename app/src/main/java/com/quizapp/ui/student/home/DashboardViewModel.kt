package com.quizapp.ui.student.home


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.quizapp.data.model.Quiz
import com.quizapp.data.repo.StudentRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repo: StudentRepo) : BaseViewModel() {
    private val _quizInfo = MutableStateFlow(Quiz())
    val quizInfo = _quizInfo.asStateFlow()
    private val _shouldNavigate = MutableStateFlow(false)
    val shouldNavigate = _shouldNavigate.asStateFlow()
    val _errorMsg = MutableStateFlow("")
    val errorMsg = _errorMsg.asStateFlow()
    fun checkQuiz(code: String) {
        viewModelScope.launch {
            try {
                val quiz = repo.getQuiz(code)
                if (quiz != null && quiz.title.isNotEmpty()) {
                    _quizInfo.value = quiz
                    _shouldNavigate.value = true
                } else {
                    Log.d("debugging", "invalid quiz from viewmodel")
                    _errorMsg.value = "Invalid quiz code. Please check if the quiz ID is correct, or check your Internet connection."
                    _errorMsg.value = ""
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error checking quiz: ${e.message}")
            }
        }
    }

    fun resetQuiz() {
        _quizInfo.value = Quiz()
        _shouldNavigate.value = false
        _errorMsg.value = ""
    }

    fun resetNavigationFlag() {
        _shouldNavigate.value = false
    }
}
