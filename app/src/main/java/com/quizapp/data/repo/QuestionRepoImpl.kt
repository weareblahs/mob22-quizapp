package com.quizapp.data.repo

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.Question
import com.quizapp.data.model.Quiz
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionRepoImpl @Inject constructor (private val authService: AuthService): QuestionRepo {
    private val db = Firebase.firestore
    private fun getQuizCollection() = db.collection("quizzes")
    private val uid = authService.getUid()

    override suspend fun getQuestionById(quizId: String, questionId: String): Question? {
        if (uid == null) return null
        val snapshots = getQuizCollection().whereEqualTo("createdBy", uid).get().await()

        for(doc in snapshots.documents) {
            val quiz = doc.toObject(Quiz::class.java)
            if (quiz != null && quiz.id == quizId) {
                for (question in quiz.questions) {
                    if (question.id == questionId) {
                        return question
                    }
                }
            }
        }
        return null
    }

    override suspend fun addQuestionToQuiz(quizId: String, question: Question) {
        if (uid == null) return
        val snapshots = getQuizCollection().whereEqualTo("createdBy", uid).get().await()

        for(doc in snapshots.documents) {
            val quiz = doc.toObject(Quiz::class.java)
            if (quiz != null && quiz.id == quizId) {
                val questions = quiz.questions.toMutableList()
                questions.add(question)
                getQuizCollection().document(quizId).update("questions", questions).await()
                return
            }
        }
    }

    override suspend fun updateQuestionInQuiz(quizId: String, question: Question) {
        if (uid == null) return
        val snapshots = getQuizCollection().whereEqualTo("createdBy", uid).get().await()

        for(doc in snapshots.documents) {
            val quiz = doc.toObject(Quiz::class.java)
            if (quiz != null && quiz.id == quizId) {
                val questions = quiz.questions.toMutableList()
                for (i in questions.indices) {
                    if (questions[i].id == question.id) {
                        questions[i] = question
                        break
                    }
                }
                getQuizCollection().document(quizId).update("questions", questions).await()
                return
            }
        }

    }

    override suspend fun deleteQuestionFromQuiz(quizId: String, questionId: String) {
        if (uid == null) return
        val snapshots = getQuizCollection().whereEqualTo("createdBy", uid).get().await()

        for(doc in snapshots.documents) {
            val quiz = doc.toObject(Quiz::class.java)
            if (quiz != null && quiz.id == quizId) {
                val questions = quiz.questions.toMutableList()
                questions.removeIf { it.id == questionId }
                getQuizCollection().document(quizId).update("questions", questions).await()
                return
            }
        }
    }


}