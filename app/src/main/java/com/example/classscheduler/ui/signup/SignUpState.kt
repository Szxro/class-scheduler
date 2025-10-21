package com.example.classscheduler.ui.signup

import com.example.classscheduler.core.ui.UiText

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isPasswordHidden: Boolean = true,
    val confirmPassword: String = "",
    val emailHasError: UiText? = null,
    val passwordHasError: UiText? = null,
    val confirmPasswordHasError: UiText? = null,
    val isLoading: Boolean = false
)
