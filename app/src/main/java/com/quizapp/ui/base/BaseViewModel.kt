package com.quizapp.ui.base

import androidx.lifecycle.ViewModel
import com.quizapp.core.utils.CustomException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel: ViewModel() {
    protected val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    suspend fun <T>errorHandler( func: suspend ()->T): T?{
        return try {
            func.invoke()
        }catch (e: CustomException) {
            _error.emit(e.message.toString())
            null
        }catch (e: IllegalArgumentException){
            _error.emit(e.message.toString())
            null
        }catch (e: Exception){
            _error.emit("Something went wrong")
            null
        }
    }
}