package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult

/**
 * Validates that a list is neither null nor empty.
 *
 * This guard clause is useful for checking collections that must contain
 * at least one element.
 *
 * @param T The type of elements in the list.
 * @param value The list to validate.
 * @param parameterName The name of the parameter being validated. Used in the
 *     default error message if no custom [message] is provided.
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether the list is valid.
 *
 * Example usage:
 * ```
 * val result = Guard.against.emptyOrNull(
 *     value = selectedItems,
 *     parameterName = "selectedItems"
 * )
 * ```
 */
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