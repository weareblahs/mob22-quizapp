package com.quizapp.ui.teacher.add_quiz.manual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quizapp.data.model.Option
import com.quizapp.data.model.Question
import com.quizapp.data.model.QuestionType
import com.quizapp.data.model.Quiz
import com.quizapp.ui.base.BaseViewModel

class ManualAddQuizViewModel : BaseViewModel() {
    private val _questions = MutableLiveData<MutableList<Question>>(mutableListOf())
    val questions: LiveData<MutableList<Question>> = _questions

    private var quizTitle: String = ""
    private var quizDescription: String = ""

    fun setQuizInfo(title: String, description: String) {
        quizTitle = title
        quizDescription = description
    }

    fun addNewQuestion() {
        val newQuestion = Question(
            text = "",
            type = QuestionType.MULTIPLE_CHOICE,
            options = listOf(
                Option(text = "", isCorrect = true),
                Option(text = "", isCorrect = false),
                Option(text = "", isCorrect = false),
                Option(text = "", isCorrect = false)
            )
        )

        val currentQuestions = _questions.value ?: mutableListOf()
        currentQuestions.add(newQuestion)
        _questions.value = currentQuestions
    }

    fun updateQuestion(position: Int, question: Question) {
        val currentQuestions = _questions.value ?: mutableListOf()
        if (position < currentQuestions.size) {
            currentQuestions[position] = question
            _questions.value = currentQuestions
        }
    }

    fun removeQuestion(position: Int) {
        val currentQuestions = _questions.value ?: mutableListOf()
        if (position < currentQuestions.size) {
            currentQuestions.removeAt(position)
            _questions.value = currentQuestions
        }
    }

    fun updateQuestionText(position: Int, text: String) {
        val currentQuestions = _questions.value ?: mutableListOf()
        if (position < currentQuestions.size) {
            val updatedQuestion = currentQuestions[position].copy(text = text)
            currentQuestions[position] = updatedQuestion
            _questions.value = currentQuestions
        }
    }

    fun updateOptionText(questionPosition: Int, optionPosition: Int, text: String) {
        val currentQuestions = _questions.value ?: mutableListOf()
        if (questionPosition < currentQuestions.size) {
            val question = currentQuestions[questionPosition]
            val options = question.options.toMutableList()
            if (optionPosition < options.size) {
                options[optionPosition] = options[optionPosition].copy(text = text)
                currentQuestions[questionPosition] = question.copy(options = options)
                _questions.value = currentQuestions
            }
        }
    }

    fun setCorrectOption(questionPosition: Int, correctOptionPosition: Int) {
        val currentQuestions = _questions.value ?: mutableListOf()
        if (questionPosition < currentQuestions.size) {
            val question = currentQuestions[questionPosition]
            val options = question.options.mapIndexed { index, option ->
                option.copy(isCorrect = index == correctOptionPosition)
            }
            currentQuestions[questionPosition] = question.copy(options = options)
            _questions.value = currentQuestions
        }
    }

    fun createQuiz(userId: String): Quiz {
        return Quiz(
            title = quizTitle,
            description = quizDescription,
            questions = _questions.value?.toList() ?: emptyList(),
            createdBy = userId
        )
    }
}