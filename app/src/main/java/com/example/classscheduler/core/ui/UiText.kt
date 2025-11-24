package com.example.classscheduler.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Represents text that can be displayed in the UI, supporting both
 * raw string values and Android string resources.
 */
sealed class UiText {
    /**
     *  Represents plain text provided directly as a raw string.
     *  */
    data class DynamicString(val value: String): UiText();

    /**
     * Represents a string resource with optional formatting arguments.
     *
     * @param resId The resource ID of the string.
     * @param args Optional formatting arguments to be resolved using the string resource.
     */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText();

    /**
     * Resolves the text into a string inside a Jetpack Compose environment.
     */
    @Composable
    fun asString() = when(this){
        is DynamicString -> value
        is StringResource -> stringResource(resId, *args);
    }

    /**
     * Resolves the text into a string using a standard Android [Context].
     */
    fun asString(context: Context) = when(this){
        is DynamicString -> value
        is StringResource -> context.getString(resId,*args);
    }
}