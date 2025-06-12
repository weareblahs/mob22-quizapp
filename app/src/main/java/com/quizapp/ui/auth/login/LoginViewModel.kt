package com.quizapp.ui.auth.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.repo.UserRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: UserRepo
): BaseViewModel() {

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo = _loginInfo.asStateFlow()

    init {
        checkLogin()
    }



    private fun checkLogin() {
        val uid = authService.getUid();
        if(uid != null) {
         viewModelScope.launch {
             val role = repo.getRole(uid)
             if(role != "null") {
                 _loginInfo.update { it.copy(role = role) };
             }
             if(loginInfo.value.role != null) {
                 _loginInfo.update { it.copy(isLogin = true) }
             }
         }
        }
    }

    fun loginWithGoogle(context: Context){
        viewModelScope.launch {
            errorHandler {
                authService.login(context)
            }?.let {
                if (it) {
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