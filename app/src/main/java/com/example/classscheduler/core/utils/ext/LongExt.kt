package com.example.classscheduler.core.utils.ext

import android.os.Build
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.TimeZone

/**
 * Converts this epoch millisecond value into a [LocalTime] using the system's default time zone.
 *
 * For devices running Android **API 31 (S)** or higher, it uses
 * [LocalTime.ofInstant]. For lower versions, it performs the conversion via
 * `LocalDateTime` and then extracts the `LocalTime`.
 *
 * This is the counterpart of [LocalTime.toEpochMilli], allowing round-trip
 * conversion between `LocalTime` and epoch millisecond representations.
 *
 * Note:
 * - The conversion depends on the system’s default time zone.
 * - Only the time portion is returned; the date is discarded during the conversion.
 *
 * @return A [LocalTime] corresponding to the moment represented by this epoch millisecond.
 */
fun Long.toLocalTime(): LocalTime{
    val instant = Instant.ofEpochMilli(this);
    val zoneId = TimeZone.getDefault().toZoneId()

    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        LocalTime.ofInstant(instant, zoneId);
    }else{
        LocalDateTime.ofInstant(instant, zoneId).toLocalTime();
    }
}