package com.quizapp.ui.auth.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.repo.QuizRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: QuizRepo
): BaseViewModel() {

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo = _loginInfo.asStateFlow()

    init {
        checkLogin()
    }



    private fun checkLogin() {
        if(authService.getUid() != null) {
            _loginInfo.update {it.copy(isLogin = true)}
            viewModelScope.launch {
                _loginInfo.update {it.copy(role = repo.getRole())}
            }
        }
    }

    fun loginWithGoogle(context: Context){
        viewModelScope.launch {
            errorHandler {
                authService.login(context)
            }?.let {
                if (it) {
                    _loginInfo.update {it.copy(isLogin = true)}
                    checkLogin()
                }
            }
        }
    }
}

data class LoginInfo (
    val isLogin: Boolean = false,
    val role: String? = null
)