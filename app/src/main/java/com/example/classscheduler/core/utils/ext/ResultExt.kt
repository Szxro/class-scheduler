package com.example.classscheduler.core.utils.ext

import com.example.classscheduler.domain.primitives.Result
import com.example.classscheduler.domain.primitives.Error

inline fun <TValue, TResult> Result<TValue>.match(
    onSuccess: (TValue?) -> TResult,
    onFailure: (Error) -> TResult
): TResult = when(this) {
    is Result.Success -> onSuccess(value)
    is Result.Failure -> onFailure(error)
}