package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult

/**
 * Validates that two string values are not equal (after trimming whitespace).
 *
 * @param value1 The first string to compare.
 * @param value2 The second string to compare.
 * @param parameterName1 Name of the first parameter (used in the default error message).
 * @param parameterName2 Name of the second parameter (used in the default error message).
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether the strings are not equal.
 *
 * Example usage:
 * ```
 * val result = Guard.against.noEqual(password, username, "password", "username")
 * ```
 */
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

/**
 * Validates that all items in a list satisfy a given [predicate].
 *
 * This can be used to check for duplicates or enforce any custom rule across
 * a collection of items.
 *
 * @param T The type of items in the list.
 * @param items The list of items to validate.
 * @param predicate A function that returns true if the item is valid.
 * @param parameterName The name of the parameter (used in the default error message).
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether all items are valid.
 *
 * Example usage:
 * ```
 * val result = Guard.against.ensureAllValid(
 *     items = emails,
 *     predicate = { it.isNotBlank() },
 *     parameterName = "emails"
 * )
 * ```
 */
fun <T> GuardClause.ensureAllValid(
    items: List<T>,
    predicate: (T) -> Boolean,
    parameterName: String,
    message: UiText? = null
): ValidationResult{
    val isValid = items.all(predicate);

    if(!isValid){
        return ValidationResult(
            isValid = false,
            errorMessage = message ?: UiText.DynamicString("The $parameterName can't contain duplicate values")
        );
    }

    return ValidationResult(isValid = true);
}


/**
 * Validates that two string values are equal (after trimming whitespace).
 *
 * @param value1 The first string to compare.
 * @param value2 The second string to compare.
 * @param parameterName1 Name of the first parameter (used in the default error message).
 * @param parameterName2 Name of the second parameter (used in the default error message).
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether the strings are equal.
 *
 * Example usage:
 * ```
 * val result = Guard.against.equal(password, confirmPassword, "password", "confirmPassword")
 * ```
 */
fun GuardClause.equal(
    value1: String,
    value2: String,
    parameterName1: String,
    parameterName2: String,
    message: UiText? = null
): ValidationResult{
    if(value1.trim() != value2.trim()){
        return ValidationResult(
            isValid = false,
            errorMessage = message ?: UiText.DynamicString("The $parameterName1 must be equal to $parameterName2")
        )
    }

    return ValidationResult(isValid = true);
}