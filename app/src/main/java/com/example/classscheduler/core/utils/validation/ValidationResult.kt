package com.example.classscheduler.core.utils.validation

import com.example.classscheduler.core.ui.UiText

data class ValidationResult(
    val errorMessage: UiText? = null,
    val isValid: Boolean = false
)