package com.example.classscheduler.ui.resetpassword

import com.example.classscheduler.core.ui.UiText

data class ResetPasswordState(
    val email:String = "",
    val emailHasError: UiText? = null,
    val isLoading: Boolean = false
)