package com.example.classscheduler.domain.models

import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Represents a weekly scheduled alarm configuration.
 *
 * @property title A short label describing the alarm's purpose.
 * @property description Additional details or context shown when the alarm triggers.
 * @property localTime The exact local time when the alarm should be triggered.
 * @property dayOfWeek The day of the week on which the alarm should occur.
 */
data class AlarmItem(
    val title: String,
    val description: String,
    val localTime: LocalTime,
    val dayOfWeek: DayOfWeek
)
