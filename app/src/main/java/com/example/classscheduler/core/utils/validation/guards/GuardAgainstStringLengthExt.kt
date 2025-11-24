package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult

/**
 * Validates that a string has at least a minimum length.
 *
 * @param input The string to validate.
 * @param minLength The minimum required length.
 * @param parameterName The name of the parameter being validated. Used in the
 *     default error message if no custom [message] is provided.
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether the string meets the minimum length.
 *
 * Example usage:
 * ```
 * val result = Guard.against.stringToShort(
 *     input = username,
 *     minLength = 3,
 *     parameterName = "username"
 * )
 * ```
 */
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

/**
 * Validates that a string does not exceed a maximum length.
 *
 * @param input The string to validate.
 * @param maxLength The maximum allowed length.
 * @param parameterName The name of the parameter being validated. Used in the
 *     default error message if no custom [message] is provided.
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether the string does not exceed the maximum length.
 *
 * Example usage:
 * ```
 * val result = Guard.against.stringToLong(
 *     input = description,
 *     maxLength = 200,
 *     parameterName = "description"
 * )
 * ```
 */
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