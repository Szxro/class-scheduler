package com.example.classscheduler.core.utils.ext

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import com.example.classscheduler.core.ui.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Shows a snackbar message in a lifecycle-aware coroutine scope.
 *
 * This function converts a [UiText] into a string using the provided [context]
 * and displays it on this [SnackbarHostState]. If a snackbar is already visible,
 * it will be dismissed before showing the new one.
 *
 * By default, the snackbar duration is [SnackbarDuration.Short], but a custom
 * [duration] can be provided.
 *
 * Example usage:
 * ```
 * snackbarHostState.showMessage(
 *     text = UiText.DynamicString("Saved successfully"),
 *     context = context,
 *     scope = coroutineScope
 * )
 * ```
 *
 * @param text The text to display in the snackbar, wrapped as [UiText].
 * @param context Android [Context] used to resolve string resources if needed.
 * @param scope [CoroutineScope] in which to launch the snackbar display.
 * @param duration Optional [SnackbarDuration]; defaults to [SnackbarDuration.Short].
 */
fun SnackbarHostState.showMessage(
    text: UiText,
    context: Context,
    scope: CoroutineScope,
    duration: SnackbarDuration? = null
){
    val message = text.asString(context);

    scope.launch {
        currentSnackbarData?.dismiss();

        showSnackbar(message,duration = duration ?: SnackbarDuration.Short);
    }
}