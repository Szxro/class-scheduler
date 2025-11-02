package com.example.classscheduler.ui.home

import com.example.classscheduler.core.ui.UiIntent

sealed class HomeIntent : UiIntent {

    object OnLogoOut : HomeIntent();
}