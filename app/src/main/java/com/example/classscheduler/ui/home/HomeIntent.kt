package com.example.classscheduler.ui.home

import com.example.classscheduler.core.ui.UiIntent

sealed class HomeIntent : UiIntent {

    object OnLogoOut : HomeIntent();

    object OnManageClassesClicked : HomeIntent();

    data class OnNavigateToDaySchedule(val day: String) : HomeIntent();
}