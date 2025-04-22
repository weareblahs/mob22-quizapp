package com.quizapp.data.repo

interface QuizRepo {
    suspend fun getRole(): String?
    suspend fun changeRole(roleType: String)
}