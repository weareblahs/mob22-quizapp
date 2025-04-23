package com.quizapp.data.repo

import com.quizapp.data.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepo {
    suspend fun getAllUserQuizzes(): Flow<List<Quiz>>
    suspend fun getQuizById(quizId: String): Quiz?
    suspend fun addQuiz(quiz: Quiz): Boolean
    suspend fun updateQuiz(quiz: Quiz)
    suspend fun deleteQuiz(quizId: String)
}