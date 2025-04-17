package com.quizapp.data.repo

import kotlinx.coroutines.flow.Flow

interface QuizRepo {
    suspend fun getRole(): String?
    suspend fun changeRole(roleType: String)
    suspend fun initializeRole()
}