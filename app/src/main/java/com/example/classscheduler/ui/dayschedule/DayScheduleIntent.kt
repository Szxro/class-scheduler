package com.example.classscheduler.ui.dayschedule

import com.example.classscheduler.core.ui.UiIntent

sealed class DayScheduleIntent : UiIntent{
    object OnNavigateToHomeScreen : DayScheduleIntent();
}