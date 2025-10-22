package com.example.classscheduler.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Nothing> {
        return authRemoteDataSource.signUpWithEmailAndPassword(email, password);
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<User> {
        return authRemoteDataSource.signInWithEmailAndPassword(email, password);
    }

    override suspend fun signInWithGoogle(context: Context): Result<User> {
        return authRemoteDataSource.signInWithGoogle(context);
    }

    override suspend fun signOut(): Result<Nothing> {
        return authRemoteDataSource.signOut();
    }
}