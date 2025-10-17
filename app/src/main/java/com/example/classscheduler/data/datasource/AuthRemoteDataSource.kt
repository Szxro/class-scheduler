package com.example.classscheduler.data.datasource

import com.example.classscheduler.domain.errors.AuthError
import com.example.classscheduler.domain.models.User
import com.example.classscheduler.domain.primitives.Result
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    val currentUser: FirebaseUser?
        get() = auth.currentUser;

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await();

            Result.onSuccess(
                User(
                    email = authResult.user?.email ?: "UNKNOWN EMAIL",
                    username = authResult.user?.displayName ?: "UNKNOWN USERNAME"
                )
            );

        } catch (exception: Exception) {
            val error = when (exception) {
                is FirebaseAuthInvalidUserException -> AuthError.UserNotFound
                is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
                else -> AuthError.UnknownError
            };

            Result.onFailure(error);
        }
    }

    suspend fun signUp(email: String, password: String): Unit {
        // Note: Can use multiple providers to create credentials (google, facebook, etc..)
        val credentials = EmailAuthProvider.getCredential(email, password);
        currentUser!!.linkWithCredential(credentials).await();
    }

    fun signOut(): Unit {
        auth.signOut();
    }
}