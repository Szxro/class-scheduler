package com.example.classscheduler.domain.interfaces

import android.content.Context
import com.example.classscheduler.domain.primitives.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    val currentIdFlow: Flow<String?>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Nothing>;

    suspend fun signInWithGoogle(context: Context): Result<Nothing>;

    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<Nothing>;

    suspend fun resetPassword(email: String) : Result<Nothing>;

    suspend fun signOut(): Result<Nothing>;
}