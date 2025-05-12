package com.quizapp.ui.teacher.add_quiz

import androidx.lifecycle.viewModelScope
import com.quizapp.data.model.Question
import com.quizapp.data.model.Quiz
import com.quizapp.data.repo.QuizRepo
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
class AddQuizViewModel  @Inject constructor(
    private val quizRepo: QuizRepo,
) : BaseViewModel() {
    private val _quizTitle = MutableStateFlow<String>("")

    private val _quizDesc = MutableStateFlow<String>("")

    private val _quizId = MutableStateFlow<String?>(null)
    val quizId: StateFlow<String?> = _quizId

    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    fun setQuizTitle(title: String) {
        _quizTitle.value = title
    }

    fun setQuizDesc(desc: String) {
        _quizDesc.value = desc
    }

    // for CSV
    fun submitQuiz(title: String, description: String, question: List<Question>) {
        viewModelScope.launch {
            errorHandler {
                val questions = question.size
                if (questions == 0) {
                    throw Exception("Please add at least one question.")
                }

                val quiz = Quiz(
                    title = title,
                    description = description,
                    questions = question,
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