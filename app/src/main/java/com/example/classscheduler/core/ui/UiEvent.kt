package com.example.classscheduler.core.ui

/**
 * Represents one-time UI events emitted by a ViewModel.
 */
sealed interface UiEvent{
    /**
     *  Requests navigation to the given [destination], optionally including arguments.
     *  */
    data class Navigate(val destination: Screen, val args: Any? = null) : UiEvent;

    /**
     *  Displays a snackbar with the provided [message].
     *  */
    data class ShowSnackBar(val message: UiText) : UiEvent;
}