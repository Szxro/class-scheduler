package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult

fun <T> GuardClause.emptyOrNull(
    value: List<T>?,
    parameterName: String,
    message: UiText? = null
): ValidationResult{
    if(!value.isNullOrEmpty()){
        return ValidationResult(isValid = true);
    }

    return ValidationResult(
        isValid = false,
        errorMessage = message ?: UiText.DynamicString("The $parameterName can't be null or empty")
    );
}