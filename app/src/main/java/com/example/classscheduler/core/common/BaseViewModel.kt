package com.example.classscheduler.core.common

import androidx.lifecycle.ViewModel
import com.example.classscheduler.core.ui.UiIntent
import com.example.classscheduler.core.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


abstract class BaseViewModel<TIntent: UiIntent>: ViewModel() {
    abstract fun onIntent(intent: TIntent): Unit;

    protected val channel: Channel<UiEvent> = Channel();

    val events = channel.receiveAsFlow();
}