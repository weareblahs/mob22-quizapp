package com.quizapp.data.repo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quizapp.core.service.AuthService
import com.quizapp.data.model.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

class UserRepoImpl @Inject constructor (): UserRepo {
    private val db = Firebase.firestore
    private fun getUserRef() : CollectionReference {
        return db.collection("users")
    }

    override suspend fun getRole(uid: String?): String {
        var returnValue = ""
        try {
            val document = uid?.let { getUserRef().document(it).get().await() }
            if(document != null) {
                if (document.exists()) {
                    val obj = document.toObject(User::class.java)
                    returnValue = obj!!.role.toString()
                } else {
                }
            }
        } catch (e: CancellationException) {
            Log.d("debugging", "${e.message}")
        } catch (e: Exception) {
            Log.d("debugging", "${e.message}")
        }
        return returnValue
    }
    override suspend fun changeRole(roleType: String, uid: String) {
        getUserRef().document(uid).set(User(role = roleType)).await()
    }
}