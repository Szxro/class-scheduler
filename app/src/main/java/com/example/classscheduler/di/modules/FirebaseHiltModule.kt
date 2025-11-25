package com.example.classscheduler.di.modules

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module that provides Firebase services used throughout the application.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseHiltModule {

    /**
     * Provides a singleton instance of [FirebaseAuth].
     *
     * The returned instance is the default Firebase authentication client
     * associated with the current FirebaseApp.
     *
     * @return The shared [FirebaseAuth] instance.
     */
    @Provides
    fun auth(): FirebaseAuth = Firebase.auth;

    /**
     * Provides a singleton instance of [FirebaseFirestore].
     *
     * This instance represents the default Cloud Firestore database
     * associated with the current FirebaseApp.
     *
     * @return The shared [FirebaseFirestore] instance.
     */
    @Provides
    fun firestore(): FirebaseFirestore = Firebase.firestore;
}