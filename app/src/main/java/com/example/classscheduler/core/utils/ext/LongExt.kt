package com.example.classscheduler.core.utils.ext

import android.os.Build
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.TimeZone

fun Long.toLocalTime(): LocalTime{
    val instant = Instant.ofEpochMilli(this);
    val zoneId = TimeZone.getDefault().toZoneId()

    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        LocalTime.ofInstant(instant, zoneId);
    }else{
        LocalDateTime.ofInstant(instant, zoneId).toLocalTime();
    }
}