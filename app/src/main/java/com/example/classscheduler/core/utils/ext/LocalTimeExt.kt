package com.example.classscheduler.core.utils.ext

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.toEpochMilli(): Long {
    val dateTime = this.atDate(LocalDate.now())

    return dateTime.atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}