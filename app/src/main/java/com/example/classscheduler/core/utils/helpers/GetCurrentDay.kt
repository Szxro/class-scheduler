package com.example.classscheduler.core.utils.helpers

import android.icu.util.Calendar

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