package com.quizapp.ui.teacher.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.repo.QuizRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repo: QuizRepo) : BaseViewModel() {

}
