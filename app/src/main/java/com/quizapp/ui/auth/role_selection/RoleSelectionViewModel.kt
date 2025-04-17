package com.quizapp.ui.auth.role_selection

import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.repo.QuizRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoleSelectionViewModel @Inject constructor(
    private val repo: QuizRepo
): BaseViewModel() {

    fun setRole(role: String) {
        viewModelScope.launch (Dispatchers.IO) {
            when(role) {
                "student" -> repo.changeRole("student")
                "teacher" -> repo.changeRole("teacher")
            }
        }
    }

}