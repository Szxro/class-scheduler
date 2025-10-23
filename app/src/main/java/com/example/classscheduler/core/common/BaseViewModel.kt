package com.example.classscheduler.core.common

import androidx.lifecycle.ViewModel
import com.example.classscheduler.core.ui.UiIntent
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.validation.Validator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


abstract class BaseViewModel<TIntent: UiIntent, TState>: ViewModel() {
    protected val channel: Channel<UiEvent> = Channel();

    protected var validator: Validator<TState>? = null;

    val events = channel.receiveAsFlow();

    abstract fun onIntent(intent: TIntent): Unit;

    fun addValidator(validator: Validator<TState>): Unit{
        this.validator = validator;
    }
}