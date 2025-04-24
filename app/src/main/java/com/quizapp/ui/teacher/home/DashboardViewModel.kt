package com.quizapp.ui.teacher.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.Option
import com.quizapp.data.model.Question
import com.quizapp.data.model.QuestionType
import com.quizapp.data.model.Quiz
import com.quizapp.data.repo.QuizRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: QuizRepo
) : BaseViewModel() {
    private val _quiz = MutableStateFlow<List<Quiz>>(emptyList())
    val quiz = _quiz.asStateFlow()

    init {
        getQuizzes()
    }

    private fun getQuizzes() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                repo.getAllUserQuizzes().collect(){
                    Log.d("Debugging", "getQuizzes: $it")
                    _quiz.value = it
                }
            }
        }
    }

    fun deleteQuiz(quiz: Quiz) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                quiz.id?.let { repo.deleteQuiz(it) }
            }
        }
    }

    fun addDummyQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val quiz = Quiz(
                    title = "Sample Quiz",
                    description = "This is a dummy quiz for testing",
                    createdBy = "", // Will be overridden in the repo with current user UID
                    questions = listOf(
                        Question(
                            id = "q1",
                            text = "What is 2 + 2?",
                            type = QuestionType.SINGLE_CHOICE,
                            options = listOf(
                                Option("3", false),
                                Option("4", true),
                                Option("5", false)
                            )
                        ),
                        Question(
                            id = "q2",
                            text = "Select the prime number:",
                            type = QuestionType.SINGLE_CHOICE,
                            options = listOf(
                                Option("4", false),
                                Option("7", true),
                                Option("8", false)
                            )
                        )
                    )
                )

                repo.addQuiz(quiz)
            }
        }
        getQuizzes()
    }


}
