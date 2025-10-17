package com.example.classscheduler.ui.signin

import com.example.classscheduler.core.ui.UiText

data class SignInState(
    val email:String = "",
    val password: String = "",
    val isPasswordHidden: Boolean = true,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val isLoading: Boolean = false,
)