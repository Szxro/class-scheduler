package com.example.classscheduler.core.utils.validation

import com.example.classscheduler.core.ui.UiText

/**
 * Represents the result of a single validation check.
 *
 * Each validation result indicates whether a field or value is valid and may
 * optionally include an error message to display if the validation fails.
 *
 * @property errorMessage Optional [UiText] representing the error message to
 *     show when [isValid] is false. Can be null if no message is needed.
 * @property isValid Indicates whether the validation passed (`true`) or failed (`false`).
 *
 * Example usage:
 * ```
 * val result = ValidationResult(
 *     errorMessage = UiText.DynamicString("Name cannot be empty"),
 *     isValid = false
 * )
 *
 * if (!result.isValid) {
 *     showError(result.errorMessage)
 * }
 * ```
 */
data class ValidationResult(
    val errorMessage: UiText? = null,
    val isValid: Boolean = false
)