package com.example.classscheduler.ui.signup

import android.content.Context
import com.example.classscheduler.core.ui.UiIntent

sealed class SignUpIntent : UiIntent{
    data class OnEmailChange(val email: String) : SignUpIntent();

    data class OnPasswordChange(val password: String): SignUpIntent();

    data class OnConfirmPasswordChange(val confirmPassword: String): SignUpIntent();

    data class OnSignUpWithGoogle(val context: Context): SignUpIntent();

    object OnSignUp : SignUpIntent();

    object OnPasswordVisibilityChange : SignUpIntent();

    object OnNavigateToSignIn : SignUpIntent();
}