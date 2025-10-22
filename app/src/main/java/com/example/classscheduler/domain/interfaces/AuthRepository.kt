package com.example.classscheduler.domain.interfaces

import android.content.Context
import com.example.classscheduler.domain.models.User
import com.example.classscheduler.domain.primitives.Result

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>;

    suspend fun signInWithGoogle(context: Context): Result<User>;

    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<Nothing>;

    suspend fun signOut(): Result<Nothing>;
}