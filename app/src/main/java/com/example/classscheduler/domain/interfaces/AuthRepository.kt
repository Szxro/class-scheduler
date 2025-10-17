package com.example.classscheduler.domain.interfaces

import com.example.classscheduler.domain.models.User
import com.example.classscheduler.domain.primitives.Result

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Unit;
    suspend fun signIn(email: String, password: String): Result<User>;
    fun signOut();
}