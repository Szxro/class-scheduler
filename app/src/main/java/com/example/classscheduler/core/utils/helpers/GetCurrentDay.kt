package com.example.classscheduler.core.utils.helpers

import android.icu.util.Calendar
import java.time.DayOfWeek

/**
 * Returns the current day of the week as a [String] in English.
 *
 * The result is one of:
 * "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday".
 *
 * This function uses [Calendar.getInstance] to determine the current day.
 *
 * @return The name of the current day of the week.
 */
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

/**
 * Converts a day name string into a [DayOfWeek] enum.
 *
 * The input string must be one of:
 * "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday".
 *
 * @param day The name of the day of the week in English.
 * @return The corresponding [DayOfWeek] enum.
 * @throws IllegalArgumentException If the provided [day] is not valid.
 */
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