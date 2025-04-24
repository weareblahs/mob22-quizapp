package com.quizapp.data.repo

import com.quizapp.data.model.Quiz

interface StudentRepo {
    suspend fun getQuiz(code: String): Quiz?
}