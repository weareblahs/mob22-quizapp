package com.quizapp.data.repo

import com.quizapp.data.model.Quiz
import com.quizapp.data.model.QuizHistory
import kotlinx.coroutines.flow.Flow

interface StudentRepo {
    suspend fun getQuiz(code: String): Quiz?
    suspend fun storeHistory(data: QuizHistory)
    suspend fun getHistory(): Flow<List<QuizHistory>>
}