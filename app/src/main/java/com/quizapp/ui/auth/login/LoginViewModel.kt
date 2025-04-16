package com.quizapp.ui.auth.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
): BaseViewModel() {
    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    fun loginWithGoogle(context: Context){
        viewModelScope.launch {
            errorHandler {
                authService.login(context)
            }?.let {
                if (it) _success.emit(Unit)
            }
        }
    }
}