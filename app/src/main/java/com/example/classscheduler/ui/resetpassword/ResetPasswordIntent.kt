package com.example.classscheduler.ui.resetpassword

import com.example.classscheduler.core.ui.UiIntent

sealed class ResetPasswordIntent : UiIntent {

    data class OnEmailChange(val email: String) : ResetPasswordIntent();

    object OnResetPasswordButtonClicked : ResetPasswordIntent();
}