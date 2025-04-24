package com.quizapp.data.repo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.Quiz
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StudentRepoImpl @Inject constructor (private val authService: AuthService): StudentRepo {
    private val db = Firebase.firestore
    private val uid = authService.getUid()
    private fun getQuizRef() : CollectionReference {
        return db.collection("quizzes")
    }

    override suspend fun getQuiz(code: String): Quiz? {
        // extra checks if the app is signed in
        if(code.isBlank()) return null
        Log.d("debugging", code)
        val snapshot = getQuizRef().document(code).get().await()
        return snapshot.toObject(Quiz::class.java)?.copy(id = snapshot.id)
    }


}