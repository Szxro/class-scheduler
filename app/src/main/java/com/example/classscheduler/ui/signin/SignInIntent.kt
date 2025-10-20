package com.example.classscheduler.ui.signin

import android.content.Context
import com.example.classscheduler.core.ui.UiIntent

sealed class SignInIntent : UiIntent {
    data class OnEmailChange(val email: String) : SignInIntent();
    data class OnPasswordChange(val password: String) : SignInIntent();

    data class OnSignInWithGoogle(val context: Context): SignInIntent();

    object OnPasswordVisibilityChange : SignInIntent();

    object OnSignInWithEmailAndPassword : SignInIntent();

    object OnNavigateToSignUp : SignInIntent();
}