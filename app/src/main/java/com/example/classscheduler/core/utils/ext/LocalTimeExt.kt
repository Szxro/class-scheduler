package com.example.classscheduler.core.utils.ext

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

fun LocalTime.toEpochMilli(): Long {
    val dateTime = this.atDate(LocalDate.now())

    return dateTime.atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}