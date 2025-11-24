package com.example.classscheduler.core.utils.ext

import com.example.classscheduler.core.utils.validation.guards.GuardClause
import com.example.classscheduler.core.utils.validation.ValidationResult

/**
 * Validates multiple [ValidationResult] objects and returns the first one
 * that represents a failure.
 *
 * Example usage:
 * ```
 * val result = guard.validateAll(
 *     validateEmail(email),
 *     validatePassword(password),
 *     validateUsername(username)
 * )
 *
 * if (!result.isValid) {
 *     showError(result.error)
 * }
 * ```
 *
 * @param results A vararg list of validation results to evaluate in order.
 * @return The first invalid [ValidationResult], or a valid result if all pass.
 */
fun GuardClause.validateAll(
    vararg results: ValidationResult,
): ValidationResult{
    for(result in results){
        if(!result.isValid) return result;
    }
    return ValidationResult(isValid = true);
}