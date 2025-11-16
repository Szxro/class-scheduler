package com.example.classscheduler.ui.dayschedule

import com.example.classscheduler.domain.models.Class

data class DayScheduleState(
    val isLoading: Boolean = false,
    val classes: List<Class> = emptyList(),
    val day: String = ""
)