package com.example.classscheduler.domain.primitives

sealed class Result<out TValue> {
    data class Success<out TValue>(val value: TValue? = null) : Result<TValue>();
    data class Failure(val error: Error) : Result<Nothing>();

    val isSuccess: Boolean
        get() = this is Success;

    val isFailure: Boolean
        get() = this is Failure;

    companion object {
        fun onSuccess(): Result<Nothing> = Success(null);

        fun <T> onSuccess(value: T): Result<T> = Success(value);

        fun onFailure(error: Error): Result<Nothing> = Failure(error);
    }
}