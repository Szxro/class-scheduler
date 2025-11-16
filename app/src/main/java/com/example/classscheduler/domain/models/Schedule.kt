package com.example.classscheduler.domain.models

data class Schedule(
    val day: String = "",
    val startTimeLong: Long? = null,
    val endTimeLong: Long? = null
)
