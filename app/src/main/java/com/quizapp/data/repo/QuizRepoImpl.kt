package com.quizapp.data.repo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizRepoImpl @Inject constructor (private val authService: AuthService): QuizRepo {
    val db = Firebase.firestore
    val uid = authService.getUid()
    private fun getUserRef() : CollectionReference {
        return db.collection("users")
    }

    private fun getQuestionSetRef() : CollectionReference {
        val uid = authService.getUid()
        return db.collection("questionSet")
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

    override suspend fun initializeRole() {
        getUserRef().document("$uid").set(User(role = "")).await()
    }
}