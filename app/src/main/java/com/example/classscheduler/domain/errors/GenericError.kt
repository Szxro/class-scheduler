package com.example.classscheduler.domain.errors

import com.example.classscheduler.R
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.domain.primitives.Error

sealed class GenericError(val error: UiText) : Error(error) {

    object UnknownError : GenericError(UiText.StringResource(R.string.generic_exception));
}