package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult
import java.util.regex.Pattern

/**
 * Validates that a string matches a given regular expression pattern.
 *
 * This guard clause first checks that the [value] is neither null nor blank
 * using [blankOrNull]. If it passes that check, it then tests the string
 * against the provided [pattern].
 *
 * @param value The string to validate.
 * @param pattern The regex pattern to match.
 * @param parameterName The name of the parameter being validated. Used in the
 *     default error message if no custom [message] is provided.
 * @param message Optional custom [UiText] error message to use if validation fails.
 * @return A [ValidationResult] indicating whether the string is valid according
 *     to the pattern.
 *
 * Example usage:
 * ```
 * val result = Guard.against.pattern(
 *     value = emailInput,
 *     pattern = PatternConstants.EMAIL_PATTERN,
 *     parameterName = "email"
 * )
 * ```
 */
fun GuardClause.pattern(
    value: String?,
    pattern: String,
    parameterName: String,
    message: UiText? = null
): ValidationResult{
    val result = Guard.against.blankOrNull(value,parameterName);

    if(!result.isValid) return result;

    val trimValue = value!!.trim();

    if(!Pattern.compile(pattern).matcher(trimValue).matches()){
        return ValidationResult(
            isValid = false,
            errorMessage = message ?: UiText.DynamicString("The $parameterName is invalid")
        );
    }

    return ValidationResult(isValid = true);
}