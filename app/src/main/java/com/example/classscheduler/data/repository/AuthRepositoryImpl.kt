package com.example.classscheduler.data.repository

import com.example.classscheduler.data.datasource.AuthRemoteDataSource
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.models.User
import com.example.classscheduler.domain.primitives.Result
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    val currentUser: FirebaseUser? = authRemoteDataSource.currentUser;

    override suspend fun signUp(email: String, password: String): Unit {
        authRemoteDataSource.signUp(email,password);
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        return authRemoteDataSource.signIn(email,password);
    }

    override fun signOut() {
        authRemoteDataSource.signOut();
    }
}