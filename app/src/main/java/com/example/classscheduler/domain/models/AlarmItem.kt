package com.example.classscheduler.domain.models

import java.time.DayOfWeek
import java.time.LocalTime

data class AlarmItem(
    val title: String,
    val description: String,
    val localTime: LocalTime,
    val dayOfWeek: DayOfWeek
)
