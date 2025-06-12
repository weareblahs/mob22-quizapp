package com.quizapp.ui.auth.role_selection

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.repo.QuizRepo
import com.quizapp.data.repo.UserRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoleSelectionViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: UserRepo
): BaseViewModel() {
    private val uid = authService.getUid();

    init {
        Log.d("debugging", uid.toString());
    }
    fun setRole(role: String, isFirstTime: Boolean) {
        viewModelScope.launch (Dispatchers.IO) {
            if(isFirstTime) {
                when(role) {
                    "student" -> uid?.let { repo.changeAllRole("student", it) }
                    "teacher" -> uid?.let { repo.changeAllRole("teacher", it) }
                }
            } else {
                when(role) {
                    "student" -> uid?.let { repo.changeRole("student", it) }
                    "teacher" -> uid?.let { repo.changeRole("teacher", it) }
                }
            }
        }
    }

}