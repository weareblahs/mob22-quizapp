package com.quizapp.ui.teacher.manage_question

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.quizapp.data.model.Question
import com.quizapp.data.model.Option
import com.quizapp.data.model.QuestionType
import com.quizapp.data.repo.QuestionRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ManageQuestionViewModel @Inject constructor(
    private val questionRepo: QuestionRepo
) : BaseViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _quizId = MutableStateFlow("")
    val quizId: StateFlow<String> = _quizId.asStateFlow()

    private val _questionId = MutableStateFlow("")
    val questionId: StateFlow<String> = _questionId.asStateFlow()

    private val _questionSavedSuccess = MutableSharedFlow<String>()
    val questionSavedSuccess = _questionSavedSuccess.asSharedFlow()


    fun initialize(quizId: String, questionId: String) {
        _quizId.value = quizId
        _questionId.value = questionId
        Log.d("ManageQuestionViewModel", "initialize: quizId: $quizId, questionId: $questionId")
        _isEditMode.value = questionId.isNotEmpty()

        if (_isEditMode.value) {
            loadQuestion()
        } else {
            _question.value = Question(
                text = "",
                type = QuestionType.SINGLE_CHOICE,
                answer = 0,
                options = List(4) { Option(text = "") }
            )
        }
    }

    private fun loadQuestion() {
        viewModelScope.launch {
            _loading.value = true
            errorHandler {
                val fetchedQuestion = questionRepo.getQuestionById(_quizId.value, _questionId.value)
                _question.value = fetchedQuestion

                // If fetched question has less than 4 options, pad with empty ones
                if (fetchedQuestion != null && fetchedQuestion.options.size < 4) {
                    val currentOptions = fetchedQuestion.options.toMutableList()
                    while (currentOptions.size < 4) {
                        currentOptions.add(Option(text = ""))
                    }
                    _question.value = fetchedQuestion.copy(options = currentOptions)
                }
            }
            _loading.value = false
        }
    }

    fun saveQuestion(questionText: String, options: List<String>, correctAnswerIndex: Int) {
        viewModelScope.launch {
            _loading.value = true
            errorHandler {
                val optionsList = options.map { Option(text = it) }

                val questionToSave = if (_isEditMode.value) {
                    _question.value?.copy(
                        text = questionText,
                        options = optionsList,
                        answer = correctAnswerIndex
                    )
                } else {
                    Question(
                        id = UUID.randomUUID().toString(),
                        text = questionText,
                        options = optionsList,
                        answer = correctAnswerIndex,
                        type = QuestionType.SINGLE_CHOICE
                    )
                }

                questionToSave?.let {
                    if (_isEditMode.value) {
                        questionRepo.updateQuestionInQuiz(_quizId.value, it)
                        _questionSavedSuccess.emit("Question updated successfully")
                    } else {
                        questionRepo.addQuestionToQuiz(_quizId.value, it)
                        _questionSavedSuccess.emit("Question added successfully")
                    }
                }
            }
            _loading.value = false
        }
    }
}