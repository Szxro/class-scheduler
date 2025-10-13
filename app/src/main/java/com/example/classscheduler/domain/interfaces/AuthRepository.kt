package com.example.classscheduler.domain.interfaces

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Unit;
    suspend fun signIn(email: String, password: String): Unit;
    fun signOut();
}