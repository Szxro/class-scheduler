package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult

fun GuardClause.stringToShort(
    input: String,
    minLength: Int,
    parameterName: String,
    message: UiText? = null
): ValidationResult{
    if(input.length < minLength){
        return ValidationResult(
            isValid = false,
            errorMessage = message ?: UiText.DynamicString("Input $parameterName with length ${input.length} is too short. Minimum Length is $minLength")
        )
    }

    return ValidationResult(isValid = true);
}

fun GuardClause.stringToLong(
    input: String,
    maxLength: Int,
    parameterName: String,
    message: UiText? = null
): ValidationResult{
    if(input.length < maxLength){
        return ValidationResult(
            isValid = false,
            errorMessage = message ?: UiText.DynamicString("Input $parameterName with length ${input.length} is too long. Max Length is $maxLength")
        )
    }

    return ValidationResult(isValid = true);
}