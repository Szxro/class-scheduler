package com.example.classscheduler.ui.signin

import com.example.classscheduler.core.ui.UiIntent

sealed class SignInIntent : UiIntent {
    data class OnEmailChange(val email: String) : SignInIntent();
    data class OnPasswordChange(val password: String) : SignInIntent();

    object OnPasswordVisibilityChange : SignInIntent();

    object OnSignIn : SignInIntent();

    data class OnSignUp(val navigateToSignUp: () -> Unit): SignInIntent();
}