package com.quizapp.data.repo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.Quiz
import com.quizapp.data.model.QuizHistory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StudentRepoImpl @Inject constructor (private val authService: AuthService): StudentRepo {
    private val db = Firebase.firestore
    private val uid = authService.getUid()
    private fun getQuizRef() : CollectionReference {
        return db.collection("quizzes")
    }
    private fun userHistoryRef() : CollectionReference {
        return db.collection("users/$uid/recentQuizzes")
    }

    override suspend fun getQuiz(code: String): Quiz? {
        // extra checks if the app is signed in
        if(code.isBlank()) return null
        val snapshot = getQuizRef().document(code).get().await()
        return snapshot.toObject(Quiz::class.java)?.copy(id = snapshot.id)
    }

    override suspend fun storeHistory(data: QuizHistory) {
        val docRef = userHistoryRef().document()
        docRef.set(data.copy(id = docRef.id)).await()
    }

    override suspend fun getHistory() = callbackFlow{
        val listener = userHistoryRef().addSnapshotListener {
                value, error ->
            if(error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val quizHistory = mutableListOf<QuizHistory>()
            value?.documents?.forEach{doc ->
                val task = doc.toObject(QuizHistory::class.java)
                if(task != null) {
                    quizHistory.add(task.copy(id = doc.id))
                }
            }
            Log.d("debugging", authService.getUid().toString())
            Log.d("debugging", quizHistory.toString())
            trySend(quizHistory)
        }
        awaitClose {
            listener.remove()
        }
    }
}