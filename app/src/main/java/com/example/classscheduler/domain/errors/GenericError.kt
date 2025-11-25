package com.example.classscheduler.domain.errors

import com.example.classscheduler.R
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.domain.primitives.Error

/**
 * Represents non-specific or uncategorized domain errors that may occur
 * during operations where no specialized error type is available.
 *
 */
sealed class GenericError(val error: UiText) : Error(error) {

    /**
     * A general-purpose error used when the cause of failure cannot be
     * identified or mapped to a more specific domain error type.
     */
    object UnknownError : GenericError(UiText.StringResource(R.string.generic_exception));
}