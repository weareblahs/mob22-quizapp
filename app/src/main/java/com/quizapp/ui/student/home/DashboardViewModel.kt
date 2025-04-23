package com.quizapp.ui.student.home


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
class DashboardViewModel @Inject constructor(private val repo: StudentRepo) : BaseViewModel() {
    private val _quizInfo = MutableStateFlow<QuizInfo>(QuizInfo())
    val quizInfo = _quizInfo.asStateFlow()
    fun loadQuiz(code: String) {

        viewModelScope.launch {
            val quiz = repo.getQuiz(code)
            _quizInfo.update {it.copy(isSearched = true, quizIsExist = (quiz != null))}
            // in case if another search is performed, reset the isSearched value to false
            _quizInfo.update {it.copy(isSearched = false)}
        }
    }
}

data class QuizInfo (
    val isSearched: Boolean = false,
    val quizIsExist: Boolean = false,
    val role: String? = null
)