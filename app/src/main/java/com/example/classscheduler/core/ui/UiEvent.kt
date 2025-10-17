package com.example.classscheduler.core.ui

sealed interface UiEvent{
    data class Navigate(val args: Any? = null) : UiEvent;

    data class ShowSnackBar(val message: UiText) : UiEvent;
}