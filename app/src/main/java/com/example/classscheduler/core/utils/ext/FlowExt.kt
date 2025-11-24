package com.example.classscheduler.core.utils.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Observes a [Flow] of events in a lifecycle-aware manner inside a composable.
 *
 * This utility is typically used for one-time UI events such as navigation,
 * snackbars, dialogs, or error messages. It automatically stops collecting
 * when the composable's lifecycle is not in the [Lifecycle.State.STARTED]
 * state, preventing leaks and unnecessary work.
 *
 * The collection always runs on the main thread using [Dispatchers.Main].
 *
 * Example usage:
 * ```
 * viewModel.events.ObserveEventsAs { event ->
 *     when (event) {
 *         is UiEvent.Navigate -> navController.navigate(event.destination)
 *         is UiEvent.ShowSnackBar -> { /* ... */ }
 *     }
 * }
 * ```
 *
 * @param onEvent Callback invoked for each emitted event.
 */
@Composable
fun<T> Flow<T>.ObserveEventsAs(onEvent: (T) -> Unit){
    val lifecycleOwner = LocalLifecycleOwner.current;
    LaunchedEffect(this,lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            withContext(Dispatchers.Main.immediate){
                this@ObserveEventsAs.collect(onEvent);
            }
        }
    }
}