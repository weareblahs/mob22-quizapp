package com.quizapp.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel: ViewModel() {
    protected val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    suspend fun <T>errorHandler(func: suspend()->T?): T? {
        return try {
            func.invoke()
        } catch(e: Exception) {
            _error.emit(e.message.toString())
            null
        }
    }
}