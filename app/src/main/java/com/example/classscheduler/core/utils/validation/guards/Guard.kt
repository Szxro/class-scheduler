package com.example.classscheduler.core.utils.validation.guards

/**
 * Marker interface for guard clauses used in validation.
 */
interface GuardClause;

/**
 * Singleton object providing access to common guard clause utilities.
 *
 * Use [Guard.against] to access validation methods for enforcing preconditions.
 *
 * Example usage:
 * ```
 * val result = Guard.against.validateAll(
 *     Guard.against.nullValue(
 *         value = someValue,
 *         parameterName = "someValue",
 *         message = UiText.DynamicString("Value cannot be null")
 *     )
 * )
 * ```
 */
object Guard : GuardClause{
    /**
     * Reference to the singleton itself, providing a fluent API for validations.
     */
    val against: GuardClause = this;
}