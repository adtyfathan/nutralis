package com.example.nutralis.data.repository

import com.example.nutralis.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun register(email: String, password: String, username: String): Result<Unit>{
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID is null")

            val user = User(uid = uid, username = username)

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

    fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

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

    suspend fun updateUserData(username: String): Result<Unit> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User ID is null")
            firestore.collection("users").document(uid)
                .update("username", username)
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