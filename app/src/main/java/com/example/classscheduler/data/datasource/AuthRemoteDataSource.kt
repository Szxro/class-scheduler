package com.example.classscheduler.data.datasource

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.example.classscheduler.core.common.BaseDataSource
import com.example.classscheduler.domain.errors.AuthError
import com.example.classscheduler.domain.errors.GenericError
import com.example.classscheduler.domain.primitives.Error
import com.example.classscheduler.domain.primitives.Result
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


/**
 * Remote data source responsible for managing all authentication-related
 * operations using Firebase Authentication and the Android Credential Manager.
 *
 * @property auth The [FirebaseAuth] instance used for all authentication operations.
 * @property credentialManager The [CredentialManager] used to interact with local device credentials.
 * @property credentialRequest The configuration used when requesting credentials from the user.
 */
class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val credentialRequest: GetCredentialRequest
) : BaseDataSource( "AuthRemoteDataSource") {
    /**
     * Returns the currently authenticated Firebase user, or null if no user is signed in.
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser;

    /**
     * A [Flow] that emits the current authenticated user's UID each time the
     * authentication state changes. Emits null when the user signs out.
     *
     * The flow registers a [FirebaseAuth.AuthStateListener] and removes it automatically
     * when the flow collector is cancelled.
     */
    val currentIdFlow : Flow<String?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener{ _ -> this.trySend(currentUser?.uid)}
            auth.addAuthStateListener(listener);
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    /**
     * Attempts to sign in a user with email and password.
     *
     * - Returns [Result.Success] if authentication succeeds and the email is verified.
     * - Returns [Result.Failure] with a specific [AuthError] on failure.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Nothing> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await();

            if(!currentUser!!.isEmailVerified){
               return Result.Failure(AuthError.EmailIsNotVerified);
            }

            Log.d(TAG,"signInWithEmailAndPassword:success");

            Result.onSuccess();

        } catch (exception: Exception) {
            Log.w(TAG,"signInWithEmailAndPassword:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    /**
     * Attempts to sign in the user using Google Sign-In through Credential Manager.
     *
     * @param context The current application context.
     * @return A [Result] representing success or a specific authentication error.
     */
    suspend fun signInWithGoogle(context: Context): Result<Nothing>{
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

            Result.onSuccess();
        }catch (exception: Exception){
            Log.w(TAG,"signInWithGoogle:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    /**
     * Creates a new user account using email and password and sends
     * a verification email to the newly created user.
     *
     * @param email The email used to create the account.
     * @param password The password chosen by the user.
     */
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<Nothing> {
        return try {
            // Create the user
            auth.createUserWithEmailAndPassword(email,password).await();

            // With the current user send a verification email
            currentUser?.sendEmailVerification()?.await();

            Log.d(TAG,"signUpWithEmailAndPassword:success");

            Result.onSuccess();
        }catch (exception: Exception){
            Log.w(TAG,"signUpWithEmailAndPassword:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    /**
     * Sends a password reset email to the specified address.
     *
     * @param email The user's email address.
     */
    suspend fun resetPassword(email: String): Result<Nothing>{
        return try {
            auth.sendPasswordResetEmail(email).await();

            Log.d(TAG,"resetPassword:success");

            Result.onSuccess();
        }catch (exception: Exception){
            Log.w(TAG,"resetPassword:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    /**
     * Signs out the current user from Firebase Authentication and clears
     * any stored credential state from Credential Manager.
     *
     * @return A [Result] indicating success or failure.
     */
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

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    override fun getErrorFromException(exception: Exception): Error {
        val error = when(exception){
            is FirebaseAuthInvalidUserException -> AuthError.UserNotFound
            is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidEmailOrPassword
            is FirebaseAuthUserCollisionException -> AuthError.EmailCollision
            is ClearCredentialException -> AuthError.CredentialsCleanUpError
            is GetCredentialCancellationException -> AuthError.CredentialsCancellation
            is NoCredentialException -> AuthError.NoCredentialsFound
            else -> GenericError.UnknownError
        }

        return error;
    }
}