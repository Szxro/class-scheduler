package com.example.classscheduler.domain.models

/**
 * Represents a scheduled time block for a class.
 *
 * @property day The day of the week for this schedule (e.g., "MONDAY", "TUESDAY").
 * @property startTimeLong The start time represented as a [Long] timestamp (milliseconds since epoch).
 *                        Can be null if not yet configured.
 * @property endTimeLong The end time represented as a [Long] timestamp (milliseconds since epoch).
 *                      Can be null if not yet configured.
 */
data class Schedule(
    val day: String = "",
    val startTimeLong: Long? = null,
    val endTimeLong: Long? = null
)
