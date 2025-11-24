package com.example.classscheduler.data.repository

import android.content.Context
import com.example.classscheduler.data.datasource.AuthRemoteDataSource
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.primitives.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override val currentUser: FirebaseUser? = authRemoteDataSource.currentUser;

    override val currentIdFlow: Flow<String?> = authRemoteDataSource.currentIdFlow;

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Nothing> {
        return authRemoteDataSource.signUpWithEmailAndPassword(email, password);
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Nothing> {
        return authRemoteDataSource.signInWithEmailAndPassword(email, password);
    }

    override suspend fun signInWithGoogle(context: Context): Result<Nothing> {
        return authRemoteDataSource.signInWithGoogle(context);
    }

    override suspend fun resetPassword(email: String): Result<Nothing> {
        return authRemoteDataSource.resetPassword(email);
    }

    override suspend fun signOut(): Result<Nothing> {
        return authRemoteDataSource.signOut();
    }
}