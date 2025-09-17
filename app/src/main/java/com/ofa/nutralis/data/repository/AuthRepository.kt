package com.ofa.nutralis.data.repository

import android.util.Log
import com.ofa.nutralis.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun register(email: String, password: String, username: String, avatar: String): Result<Unit>{
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID is null")

            val user = User(uid = uid, username = username, avatar = avatar)

            firestore.collection("users").document(uid).set(user).await()

            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit>{
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    fun logout(){
        firebaseAuth.signOut()
    }

    suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID is null")

            val userDoc = firestore.collection("users").document(uid).get().await()
            if (!userDoc.exists()) {
                val user = User(
                    uid = uid,
                    username = authResult.user?.displayName ?: "User-${uid}",
                    avatar = "avatar1"
                )
                firestore.collection("users").document(uid).set(user).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Google sign-in failed", e)
            Result.failure(e)
        }
    }


    suspend fun getUserData(): Result<User> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User ID is null")
            val snapshot = firestore.collection("users").document(uid).get().await()
            val user = snapshot.toObject(User::class.java) ?: throw Exception("User not found")
            Result.success(user)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun updateUserData(username: String, avatar: String): Result<Unit> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User ID is null")
            firestore.collection("users").document(uid)
                .update(
                    "username", username,
                    "avatar", avatar
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUserData(): Result<Unit> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User ID is null")
            firestore.collection("users").document(uid).delete().await()
            firebaseAuth.currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}