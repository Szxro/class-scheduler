package com.example.classscheduler.core.utils.ext

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalTime
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.S)
fun Long.toLocalTime(): LocalTime{
    return LocalTime.ofInstant(Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId());
}