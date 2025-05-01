package com.quizapp.ui.quiz

import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.Question
import com.quizapp.data.model.Quiz
import com.quizapp.data.repo.QuizRepo
import com.quizapp.data.repo.UserRepo

import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManualAddQuizViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService
) : BaseViewModel() {

    private val _questionList = MutableStateFlow<List<Question>>(emptyList())
    val questionList: StateFlow<List<Question>> = _questionList.asStateFlow()

    private val _quizId = MutableStateFlow<String?>(null)
    val quizId: StateFlow<String?> = _quizId

    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    fun addQuestion(question: Question) {
        _questionList.value += question
    }

    fun submitQuiz(title: String, description: String) {
        viewModelScope.launch {
            errorHandler {
                val questions = _questionList.value
                if (questions.isEmpty()) {
                    throw Exception("Please add at least one question.")
                }

                val quiz = Quiz(
                    title = title,
                    description = description,
                    questions = questions,
                )

                val id = quizRepo.addQuiz(quiz)
                if (id.isNotEmpty()) {
                    _quizId.emit(id)
                    _success.emit(Unit)
                } else {
                    throw Exception("Failed to create quiz.")
                }
            }
        }
    }
}
