package com.quizapp.data.repo

interface UserRepo {
    suspend fun getRole(uid: String?): String
    suspend fun changeRole(roleType: String, uid: String)
    suspend fun changeAllRole(roleType: String, uid: String)
    suspend fun getPreviousRole(uid: String?): String
}