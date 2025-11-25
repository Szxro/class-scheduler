package com.example.classscheduler.data.repository

import android.content.Context
import com.example.classscheduler.data.datasource.AuthRemoteDataSource
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.primitives.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [AuthRepository] that delegates authentication operations
 * to the remote data source [AuthRemoteDataSource], which internally uses
 * Firebase Authentication and Credential Manager.
 *
 * @property authRemoteDataSource The underlying data source handling remote authentication logic.
 */
class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    /**
     * The currently authenticated Firebase user, or null
     * if no user is signed in.
     */
    override val currentUser: FirebaseUser? = authRemoteDataSource.currentUser;

    /**
     * A reactive [Flow] emitting the authenticated user's UID whenever the
     * authentication state changes. Emits null when the user logs out.
     */
    override val currentIdFlow: Flow<String?> = authRemoteDataSource.currentIdFlow;

    /**
     * Creates a new user account using email and password.
     *
     * @param email The user's email address.
     * @param password The chosen password.
     * @return A [Result] representing success or failure.
     */
    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Nothing> {
        return authRemoteDataSource.signUpWithEmailAndPassword(email, password);
    }

    /**
     * Attempts to sign in a user using email and password credentials.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] indicating whether the sign-in operation succeeded or failed.
     */
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Nothing> {
        return authRemoteDataSource.signInWithEmailAndPassword(email, password);
    }

    /**
     * Attempts to authenticate the user using Google Sign-In via Credential Manager.
     *
     * @param context The current application context, required to display
     *                credential UI to the user.
     * @return A [Result] indicating success or a specific authentication error.
     */
    override suspend fun signInWithGoogle(context: Context): Result<Nothing> {
        return authRemoteDataSource.signInWithGoogle(context);
    }

    /**
     * Sends a password reset email to the specified address.
     *
     * @param email The user's email address.
     * @return A [Result] indicating whether the reset request succeeded.
     */
    override suspend fun resetPassword(email: String): Result<Nothing> {
        return authRemoteDataSource.resetPassword(email);
    }

    /**
     * Signs out the currently authenticated user and clears any stored
     * credential state.
     *
     * @return A [Result] representing the outcome of the sign-out operation.
     */
    override suspend fun signOut(): Result<Nothing> {
        return authRemoteDataSource.signOut();
    }
}