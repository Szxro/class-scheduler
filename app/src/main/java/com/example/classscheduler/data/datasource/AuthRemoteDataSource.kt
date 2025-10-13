package com.example.classscheduler.data.datasource

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    // returns the current user that is sign in if not (null)
    val currentUser: FirebaseUser?
        get() = auth.currentUser;

    /*
    * Sign in existing users
    * */
    suspend fun signIn(email: String, password: String): Unit {
        auth.signInWithEmailAndPassword(email, password).await();
    }

    /*
    * Create a new user with an email and password
    * */
    suspend fun signUp(email: String, password: String): Unit {
        // Note: Can use multiple providers to create credentials (google, facebook, etc..)
        val credentials = EmailAuthProvider.getCredential(email, password);
        currentUser!!.linkWithCredential(credentials).await();
    }

    /*
    *  Sign out of the current user
    * */
    fun signOut():Unit{
        auth.signOut();
    }
}