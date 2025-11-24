package com.example.classscheduler.core.common

import androidx.lifecycle.ViewModel
import com.example.classscheduler.core.ui.UiIntent
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.validation.Validator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<TIntent: UiIntent, TState>: ViewModel() {
    /**
     *  Internal channel used to send one-shot UI events to the UI layer.
     *  */
    protected val channel: Channel<UiEvent> = Channel();

    /**
     * Optional validator used to validate [TState] instances.
     * ViewModels may attach a validator via [addValidator] when needed.
     */
    protected var validator: Validator<TState>? = null;

    /**
     *  Flow used by the UI to observe one-shot UI events emitted by the ViewModel.
     * */
    val events = channel.receiveAsFlow();

    /**
     * Processes an incoming UI intent.
     *
     * Each concrete ViewModel must implement how it reacts to specific intents
     * (e.g., updating state, navigating, validating, saving data, etc.).
     *
     * @param intent The UI intent to handle.
     */
    abstract fun onIntent(intent: TIntent): Unit;

    /**
     * Assigns a [Validator] that can be used to validate the current UI state.
     *
     * Calling this will replace any previously assigned validator.
     *
     * @param validator The validator to attach to this ViewModel.
     */
    fun addValidator(validator: Validator<TState>): Unit{
        this.validator = validator;
    }
}