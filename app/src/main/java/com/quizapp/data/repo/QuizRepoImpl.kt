package com.quizapp.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizRepoImpl @Inject constructor (val authService: AuthService): QuizRepo {
    private val db = Firebase.firestore
    private val uid = authService.getUid()
    private fun getUserRef() : CollectionReference {
        return db.collection("users")
    }

    override suspend fun getRole(): String? {
        val snapshot = getUserRef().document("$uid").get().await()
        val user = snapshot.toObject(User::class.java)
        return if(user == null) {
            ""
        } else {
            user.role
        }
    }

    override suspend fun changeRole(roleType: String) {
        getUserRef().document("$uid").set(User(role = roleType)).await()
    }
}