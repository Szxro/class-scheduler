package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult

fun GuardClause.blankOrNull(
    value: String?,
    parameterName: String,
    message: UiText? = null
): ValidationResult {
    if(!value.isNullOrBlank()){
        return ValidationResult(isValid = true);
    }
    return ValidationResult(
        isValid = false,
        errorMessage = message ?: UiText.DynamicString("The $parameterName can't be blank")
    )
}

fun <T> GuardClause.nullValue(
    value: T?,
    parameterName: String,
    message: UiText? = null
): ValidationResult {
    if(value !== null) return ValidationResult(isValid = true)

    return ValidationResult(
        isValid = false,
        errorMessage = message ?: UiText.DynamicString("The $parameterName can't be null")
    );
}