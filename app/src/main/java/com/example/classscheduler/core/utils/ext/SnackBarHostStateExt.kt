package com.example.classscheduler.core.utils.ext

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import com.example.classscheduler.core.ui.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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