package com.example.classscheduler.domain.primitives

/**
 * Represents the outcome of an operation, encapsulating either a success
 * with an optional value or a failure with an associated [Error].
 *
 * This class provides a standardized way to handle success and failure
 * across the domain and data layers without relying on exceptions.
 *
 * @param TValue The type of the value returned on a successful operation.
 */
sealed class Result<out TValue> {
    /**
     * Represents a successful operation.
     *
     * @property value The value produced by the operation, or `null` if no value is returned.
     */
    data class Success<out TValue>(val value: TValue? = null) : Result<TValue>();

    /**
     * Represents a failed operation.
     *
     * @property error The [Error] that caused the operation to fail.
     */
    data class Failure(val error: Error) : Result<Nothing>();

    /**
     * Returns true if this [Result] represents a successful operation.
     */
    val isSuccess: Boolean
        get() = this is Success;

    /**
     * Returns true if this [Result] represents a failed operation.
     */
    val isFailure: Boolean
        get() = this is Failure;

    companion object {

        /**
         * Creates a [Result.Success] with no associated value.
         *
         * @return A successful [Result] with `null` value.
         */
        fun onSuccess(): Result<Nothing> = Success(null);


        /**
         * Creates a [Result.Success] with the provided [value].
         *
         * @param T The type of the success value.
         * @param value The value to wrap in the success result.
         * @return A successful [Result] containing the value.
         */
        fun <T> onSuccess(value: T): Result<T> = Success(value);

        /**
         * Creates a [Result.Failure] wrapping the provided [error].
         *
         * @param error The domain-specific [Error] causing the failure.
         * @return A failure [Result] containing the error.
         */
        fun onFailure(error: Error): Result<Nothing> = Failure(error);
    }
}