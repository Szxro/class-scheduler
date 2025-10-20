package com.example.classscheduler.data.datasource

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.classscheduler.domain.errors.AuthError
import com.example.classscheduler.domain.models.User
import com.example.classscheduler.domain.primitives.Result
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val credentialRequest: GetCredentialRequest
) {
    private val TAG: String = "AuthRemoteDataSource";
    val currentUser: FirebaseUser?
        get() = auth.currentUser;

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await();

            Log.d(TAG,"signInWithEmailAndPassword:success");

            Result.onSuccess(
                User(
                    email = authResult.user?.email ?: "UNKNOWN EMAIL",
                    username = authResult.user?.displayName ?: "UNKNOWN USERNAME"
                )
            );

        } catch (exception: Exception) {
            Log.w(TAG,"signInWithEmailAndPassword:failure", exception);

            val error = when (exception) {
                is FirebaseAuthInvalidUserException -> AuthError.UserNotFound
                is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidEmailOrPassword
                else -> AuthError.UnknownError
            };

            Result.onFailure(error);
        }
    }

    suspend fun signInWithGoogle(context: Context): Result<User>{
        return try {
            // Request a credential from the user
            val credentialResponse = credentialManager.getCredential(context,credentialRequest);

            if(credentialResponse.credential !is CustomCredential && credentialResponse.credential.type != TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                Result.onFailure(AuthError.UnknownCredentials);
            }
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialResponse.credential.data);

            // Obtaining the firebase credentials
            val credentials = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null);

            // Sign in with the credentials provide by the  user
            val authResult = auth.signInWithCredential(credentials).await();

            Log.d(TAG,"signInWithGoogle:success");

            Result.onSuccess(User(
                email =  authResult.user?.email ?: "UNKNOWN EMAIL",
                username = authResult.user?.displayName ?: "UNKNOWN USERNAME"
            ));
        }catch (exception: Exception){
            Log.w(TAG,"signInWithGoogle:failure", exception);

            val error = when(exception){
                is GetCredentialCancellationException -> AuthError.CredentialsCancellation
                is NoCredentialException -> AuthError.NoCredentialsFound
                is GetCredentialException -> AuthError.CredentialError
                else -> AuthError.UnknownError
            }
            Result.onFailure(error);
        }
    }

    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email,password).await();

            Log.d(TAG,"signUpWithEmailAndPassword:success");

            Result.onSuccess(User(
                username = authResult.user?.displayName ?: "UNKNOWN USERNAME",
                email = authResult.user?.email ?: "UNKNOWN EMAIL"
            ));
        }catch (exception: Exception){
            Log.w(TAG,"signUpWithEmailAndPassword:failure", exception);

            val error = when(exception){
                else -> AuthError.UnknownError
            }
            Result.onFailure(error);
        }
    }

    suspend fun signOut(): Result<Nothing> {
        return try {
            // Firebase Sign out
            auth.signOut();

            // Clear the current user credentials from all credentials providers
            val clearRequest = ClearCredentialStateRequest();

            credentialManager.clearCredentialState(clearRequest);

            Log.d(TAG,"signOut:success");

            Result.onSuccess();
        }catch (exception: Exception){
            Log.w(TAG,"signOut:failure", exception);

            val error = when(exception){
                is ClearCredentialException -> AuthError.CredentialsCleanUpError
                else -> AuthError.UnknownError
            };

            Result.onFailure(error);
        }
    }
}