package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult

/**
 * Checks whether a [String] value is neither null nor blank.
 *
 * This is a guard clause used for validating text input.
 *
 * @param value The string to validate.
 * @param parameterName The name of the parameter being validated. Used in the
 *     default error message if no custom [message] is provided.
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether the string is valid and
 *     containing an error message if it is invalid.
 *
 * Example usage:
 * ```
 * val result = Guard.against.blankOrNull(
 *     value = nameInput,
 *     parameterName = "name",
 *     message = UiText.DynamicString("Name is required")
 * )
 * ```
 */
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

/**
 * Checks whether a value is not null.
 *
 * This is a generic guard clause used to validate any nullable type.
 *
 * @param T The type of the value being validated.
 * @param value The value to validate.
 * @param parameterName The name of the parameter being validated. Used in the
 *     default error message if no custom [message] is provided.
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether the value is valid and
 *     containing an error message if it is invalid.
 *
 * Example usage:
 * ```
 * val result = Guard.against.nullValue(
 *     value = selectedClass,
 *     parameterName = "selectedClass",
 *     message = UiText.DynamicString("You must select a class")
 * )
 * ```
 */
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