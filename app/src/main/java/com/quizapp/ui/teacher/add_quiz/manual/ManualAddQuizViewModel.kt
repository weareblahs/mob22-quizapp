package com.quizapp.ui.teacher.add_quiz.manual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.quizapp.data.model.Option
import com.quizapp.data.model.Question
import com.quizapp.data.model.QuestionType
import com.quizapp.data.model.Quiz
import com.quizapp.data.repo.QuizRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManualAddQuizViewModel @Inject constructor(
    private val repo: QuizRepo
) : BaseViewModel() {
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    private var quizTitle: String = ""
    private var quizDescription: String = ""

    fun setQuizInfo(title: String, description: String) {
        quizTitle = title
        quizDescription = description
    }

    fun addNewQuestion() {
        viewModelScope.launch{
            val updated = _questions.value.toMutableList()
            updated.add(
                Question(
                    text = "",
                    type = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        Option(text = "", isCorrect = true),
                        Option(text = "", isCorrect = false),
                        Option(text = "", isCorrect = false),
                        Option(text = "", isCorrect = false)
                    )
                )
            )
            _questions.value = updated
        }
    }
    fun updateQuestion(position: Int, question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val updated = _questions.value.toMutableList()
                if (position in updated.indices) {
                    updated[position] = question
                    _questions.value = updated
                }
            }
        }
    }

    fun removeQuestion(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val updated = _questions.value.toMutableList()
                if (position in updated.indices) {
                    updated.removeAt(position)
                    _questions.value = updated
                }
            }
        }
    }

    fun updateQuestionType(position: Int, isMultipleChoice: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val updated = _questions.value.toMutableList()
                if (position in updated.indices) {
                    val questionType = if (isMultipleChoice) QuestionType.MULTIPLE_CHOICE else QuestionType.SINGLE_CHOICE
                    updated[position] = updated[position].copy(type = questionType)
                    _questions.value = updated
                }
            }
        }
    }

    fun updateQuestionText(position: Int, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val updated = _questions.value.toMutableList()
                if (position in updated.indices) {
                    val updatedQuestion = updated[position].copy(text = text)
                    updated[position] = updatedQuestion
                    _questions.value = updated
                }
            }
        }
    }

    fun updateOptionText(questionPosition: Int, optionPosition: Int, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val updated = _questions.value.toMutableList()
                if (questionPosition in updated.indices) {
                    val question = updated[questionPosition]
                    val options = question.options.toMutableList()
                    if (optionPosition in options.indices) {
                        options[optionPosition] = options[optionPosition].copy(text = text)
                        updated[questionPosition] = question.copy(options = options)
                        _questions.value = updated
                    }
                }
            }
        }
    }

    fun setCorrectOption(questionPosition: Int, correctOptionPosition: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val updated = _questions.value.toMutableList()
                if (questionPosition in updated.indices) {
                    val question = updated[questionPosition]
                    val newOptions = question.options.mapIndexed { index, option ->
                        option.copy(isCorrect = index == correctOptionPosition)
                    }
                    updated[questionPosition] = question.copy(options = newOptions)
                    _questions.value = updated
                }
            }
        }
    }

    fun createQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val quiz = Quiz(
                    title = quizTitle,
                    description = quizDescription,
                    questions = _questions.value
                )
                 repo.addQuiz(quiz)

            }?.let {
                if (it) _success.emit(Unit)
            }
        }
    }

}