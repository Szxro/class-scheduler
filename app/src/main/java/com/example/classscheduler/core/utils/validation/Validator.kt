package com.example.classscheduler.core.utils.validation

/**
 * Base class for implementing validators for different types of data.
 *
 * A validator is responsible for checking one or more properties of a given
 * object of type [T] and returning validation results.
 *
 * Subclasses should override [validate] to implement the specific validation
 * rules for the type [T].
 *
 * Each validation result is stored in [validations], mapping a property name
 * or key to a [ValidationResult]. This allows multiple fields to be validated
 * at once and for their error messages or status to be retrieved.
 *
 * Example usage:
 * ```
 * class CreateClassValidator : Validator<CreateClassState>() {
 *     override fun validate(value: CreateClassState): Map<String, ValidationResult> {
 *         // Implement validation rules here
 *     }
 * }
 * ```
 *
 * @param T The type of object this validator validates.
 */
 abstract class Validator<T> {
    /**
     * Stores the results of individual validations, keyed by property name.
     */
     protected val validations: MutableMap<String, ValidationResult> = mutableMapOf();

    /**
     * Validates the given [value] and returns a map of field names to [ValidationResult].
     *
     * @param value The object to validate.
     * @return A map where each key is a field name and the value is the corresponding
     *         [ValidationResult] indicating whether that field is valid and any
     *         associated error message.
     */
     abstract fun validate(value: T): Map<String, ValidationResult>;
}