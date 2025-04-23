package com.quizapp.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.Quiz
import com.quizapp.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizRepoImpl @Inject constructor (val authService: AuthService): QuizRepo {
    private val db = Firebase.firestore
    private fun getQuizCollection() = db.collection("quizzes")
    private val uid = authService.getUid()

    override suspend fun getAllUserQuizzes() = callbackFlow{
        // Check if the user is logged in, if not, close the flow
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        // Listen for changes in the quizzes collection where the createdBy field matches the user's UID
        val listener = getQuizCollection().whereEqualTo("createdBy", uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val quizzes = mutableListOf<Quiz>()
                value?.documents?.forEach { doc ->
                    val quiz = doc.toObject(Quiz::class.java)
                    if (quiz != null) {
                        quizzes.add(quiz.copy(id = doc.id))
                    }
                }

                trySend(quizzes)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getQuizById(quizId: String): Quiz? {
        val snapshot = getQuizCollection().document(quizId).get().await()
        val quiz = snapshot.toObject(Quiz::class.java)?.copy(id = snapshot.id)

        // Check if the quiz exists and if the user is the creator
        // If the quiz doesn't exist or the user is not the creator, return null
        return if (quiz?.createdBy == uid) {
            quiz
        } else {
            null
        }
    }

    override suspend fun addQuiz(quiz: Quiz) {
        if (uid == null) return

        val id = generateUnique6DigitId()

        val docRef = getQuizCollection().document(id)
        val quizCopy = quiz.copy(id = id, createdBy = uid)
        docRef.set(quizCopy).await()
    }

    private suspend fun generateUnique6DigitId(): String {
        val charset = ('A'..'Z') + ('0'..'9')
        var id: String
        var docExists: Boolean

        do {
            id = List(6) { charset.random() }.joinToString("")
            val docRef = getQuizCollection().document(id)
            docExists = docRef.get().await().exists()
        } while (docExists)

        return id
    }

    override suspend fun updateQuiz(quiz: Quiz) {
        if (uid == null || quiz.id == null) return

        val existing = getQuizById(quiz.id)
        if (existing?.createdBy != uid) return

        getQuizCollection().document(quiz.id).set(quiz).await()
    }

    override suspend fun deleteQuiz(quizId: String) {
        if (uid == null) return

        val existing = getQuizById(quizId)
        if (existing?.createdBy != uid) return

        getQuizCollection().document(quizId).delete().await()
    }
}