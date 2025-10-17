package com.example.classscheduler.core.utils.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// Utility for safely collecting events from a Flow in a lifecycle-aware manner. (commonly use in one-time-events)
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