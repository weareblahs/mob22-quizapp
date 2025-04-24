package com.quizapp.ui.teacher.add_quiz.manual

import androidx.lifecycle.viewModelScope
import com.quizapp.data.model.Option
import com.quizapp.data.model.Question
import com.quizapp.data.model.Quiz
import com.quizapp.data.repo.QuizRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _quizId = MutableStateFlow<String>("")
    val quizId = _quizId.asStateFlow()

    private val _validationError = MutableSharedFlow<String>()
    val validationError = _validationError.asSharedFlow()

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

    fun onCorrectOptionChanged(questionPosition: Int, optionPosition: Int, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val updated = _questions.value.toMutableList()
                if (questionPosition in updated.indices) {
                    val question = updated[questionPosition]
                    val options = question.options.toMutableList()

                    if (optionPosition in options.indices) {
                        // For single choice questions, ensure only one option is selected
                        for (i in options.indices) {
                            options[i] = options[i].copy(isCorrect = i == optionPosition && isChecked)
                        }

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

                    // Set only this option as correct
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
                // Validate questions
                if (_questions.value.isEmpty()) {
                    _validationError.emit("Please add at least one question")
                    return@errorHandler
                }

                for ((index, question) in _questions.value.withIndex()) {
                    // Validate question text
                    if (question.text.isBlank()) {
                        _validationError.emit("Question ${index + 1} is empty")
                        return@errorHandler
                    }

                    // Validate options
                    for ((optIndex, option) in question.options.withIndex()) {
                        if (option.text.isBlank()) {
                            _validationError.emit("Option ${optIndex + 1} in Question ${index + 1} is empty")
                            return@errorHandler
                        }
                    }

                    // Validate if exactly one correct answer is selected
                    if (question.options.count { it.isCorrect } != 1) {
                        _validationError.emit("Please select exactly one correct answer for Question ${index + 1}")
                        return@errorHandler
                    }
                }

                val quiz = Quiz(
                    title = quizTitle,
                    description = quizDescription,
                    questions = _questions.value
                )
                val quizId = repo.addQuiz(quiz)
                _quizId.value = quizId
                _success.emit(Unit)
            }
        }
    }
}