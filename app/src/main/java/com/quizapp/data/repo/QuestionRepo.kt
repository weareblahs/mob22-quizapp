package com.quizapp.data.repo

import com.quizapp.data.model.Question

interface QuestionRepo {
    suspend fun getQuestionById(quizId: String, questionId: String): Question?
    suspend fun addQuestionToQuiz(quizId: String, question: Question)
    suspend fun updateQuestionInQuiz(quizId: String, question: Question)
    suspend fun deleteQuestionFromQuiz(quizId: String, questionId: String)
}
