package com.quizapp.ui.student.home


import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.Quiz
import com.quizapp.data.model.QuizHistory
import com.quizapp.data.repo.StudentRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val authService: AuthService, private val repo: StudentRepo) : BaseViewModel() {
    private val _quizInfo = MutableStateFlow(Quiz())
    val quizInfo = _quizInfo.asStateFlow()
    private val _shouldNavigate = MutableStateFlow(false)
    val shouldNavigate = _shouldNavigate.asStateFlow()
    private val _errorMsg = MutableStateFlow("")
    val errorMsg = _errorMsg.asStateFlow()
    private val _logout = MutableStateFlow(false)
    val logout = _logout.asStateFlow()
    private val _quizHistory = MutableStateFlow<List<QuizHistory>>(emptyList())
    val quizHistory = _quizHistory.asStateFlow()

    init {
        getQuizzes()
    }

    fun checkQuiz(code: String) {
        viewModelScope.launch {
            try {
                val quiz = repo.getQuiz(code)
                if (quiz != null && quiz.title.isNotEmpty()) {
                    _quizInfo.value = quiz
                    _shouldNavigate.value = true
                } else {
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
        _quizHistory.value = emptyList()
    }

    fun resetNavigationFlag() {
        _shouldNavigate.value = false
    }

    fun getProfileUrl(): Uri? {
        return authService.getLoggedInUser()?.photoUrl
    }

    fun logout() {
        viewModelScope.launch {
            authService.logout()
            _logout.update {true}
        }
    }

    private fun getQuizzes() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                repo.getHistory().collect{
                    _quizHistory.value = it
                }
            }
        }
    }
}
