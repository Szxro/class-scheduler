package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult

fun GuardClause.noEqual(
    value1: String,
    value2: String,
    parameterName1: String,
    parameterName2: String,
    message: UiText? = null
): ValidationResult{
    if(value1.trim() != value2.trim()){
        return ValidationResult(isValid = true);
    }

    return ValidationResult(
        isValid = false,
        errorMessage = message ?: UiText.DynamicString("The $parameterName1 can't be equal to $parameterName2")
    )
}

fun GuardClause.equal(
    value1: String,
    value2: String,
    parameterName1: String,
    parameterName2: String,
    message: UiText? = null
): ValidationResult{
    if(value1.trim() != value2.trim()){
        return ValidationResult(
            isValid = true,
            errorMessage = message ?: UiText.DynamicString("The $parameterName1 must be equal to $parameterName2")
        )
    }

    return ValidationResult(isValid = true);
}