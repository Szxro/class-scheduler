package com.example.classscheduler.core.utils.helpers

import android.icu.util.Calendar
import java.time.DayOfWeek
fun getCurrentDay():String{
    val calendar = Calendar.getInstance();
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    val day = when(dayOfWeek){
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        Calendar.SUNDAY -> "Sunday"
        else -> ""
    }

    return day;
}

fun getCurrentDay(day: String): DayOfWeek{
    val dayOfTheWeek = when(day){
        "Monday" -> DayOfWeek.MONDAY
        "Tuesday" -> DayOfWeek.TUESDAY
        "Wednesday" -> DayOfWeek.WEDNESDAY
        "Thursday" -> DayOfWeek.THURSDAY
        "Friday" -> DayOfWeek.FRIDAY
        "Saturday" -> DayOfWeek.SATURDAY
        "Sunday" -> DayOfWeek.SUNDAY
        else -> throw IllegalArgumentException("Invalid day name: $day")
    }

    return dayOfTheWeek
}