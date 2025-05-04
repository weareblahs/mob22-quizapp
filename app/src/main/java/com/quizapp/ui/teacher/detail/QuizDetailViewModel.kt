package com.quizapp.ui.teacher.detail

import androidx.lifecycle.viewModelScope
import com.quizapp.data.model.Quiz
import com.quizapp.data.model.Question
import com.quizapp.data.repo.QuizRepo
import com.quizapp.data.repo.QuestionRepo
import com.quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val questionRepo: QuestionRepo
) : BaseViewModel() {

    private val _quizId = MutableStateFlow("")
    val quizId: StateFlow<String> = _quizId.asStateFlow()

    private val _quizDetail = MutableStateFlow<Quiz?>(null)
    val quizDetail: StateFlow<Quiz?> = _quizDetail.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun setQuizId(id: String) {
        _quizId.value = id
        getQuizDetail()
    }

    fun getQuizDetail() {
        viewModelScope.launch {
            _loading.value = true
            errorHandler {
                val quiz = quizRepo.getQuizById(_quizId.value)
                quiz?.let {
                    _quizDetail.value = it
                    _questions.value = it.questions
                }
            }
            _loading.value = false
        }
    }

    fun addQuestion(question: Question) {
        viewModelScope.launch {
            _loading.value = true
            errorHandler {
                questionRepo.addQuestionToQuiz(_quizId.value, question)
                getQuizDetail()
            }
            _loading.value = false
        }
    }

    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            _loading.value = true
            errorHandler {
                questionRepo.updateQuestionInQuiz(_quizId.value, question)
                getQuizDetail()
            }
            _loading.value = false
        }
    }

    fun deleteQuestion(questionId: String) {
        viewModelScope.launch {
            _loading.value = true
            errorHandler {
                questionRepo.deleteQuestionFromQuiz(_quizId.value, questionId)
                getQuizDetail()
            }
            _loading.value = false
        }
    }

    fun updateQuiz(quiz: Quiz) {
        viewModelScope.launch {
            _loading.value = true
            errorHandler {
                quizRepo.updateQuiz(quiz)
                getQuizDetail()
            }
            _loading.value = false
        }
    }

}
