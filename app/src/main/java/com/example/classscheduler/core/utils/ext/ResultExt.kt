package com.example.classscheduler.core.utils.ext

import com.example.classscheduler.domain.primitives.Result
import com.example.classscheduler.domain.primitives.Error

/**
 * Handles a [Result] by providing callbacks for both success and failure cases.
 *
 * This function allows you to safely extract the value or error from a [Result]
 * and transform it into a desired type ([TResult]) in a concise way.
 *
 * Usage example:
 * ```
 * val result: Result<String> = repository.getData()
 *
 * result.match(
 *     onSuccess = { value -> println("Data: $value") },
 *     onFailure = { error -> println("Error: ${error.message}") }
 * )
 * ```
 *
 * @param onSuccess Callback invoked if the [Result] is [Result.Success].
 *        The value may be `null` if the underlying type allows it.
 * @param onFailure Callback invoked if the [Result] is [Result.Failure].
 *        Provides the [Error] instance.
 * @return The result of either [onSuccess] or [onFailure] as type [TResult].
 */
inline fun <TValue, TResult> Result<TValue>.match(
    onSuccess: (TValue?) -> TResult,
    onFailure: (Error) -> TResult
): TResult = when(this) {
    is Result.Success -> onSuccess(value)
    is Result.Failure -> onFailure(error)
}