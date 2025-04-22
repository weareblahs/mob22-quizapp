package com.quizapp.data.repo

interface UserRepo {
    suspend fun getRole(): String?
    suspend fun changeRole(roleType: String)
}