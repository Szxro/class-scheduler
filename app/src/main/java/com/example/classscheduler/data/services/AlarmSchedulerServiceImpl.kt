package com.example.classscheduler.data.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.classscheduler.domain.interfaces.AlarmSchedulerService
import com.example.classscheduler.domain.models.AlarmItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlarmSchedulerService {
    private val _alarmManager = context.getSystemService(AlarmManager::class.java);

    override fun scheduleWeeklyAlarm(item: AlarmItem) {
        val ( title, description , localtime, dayOfTheWeek ) = item;

        val triggerTime = getNextTriggerTime(dayOfTheWeek, localtime);

        // TODO: this intent are going to be send to the receiver
        val intent = Intent().apply {
            putExtra("title", title);
            putExtra("description", description);
            putExtra("localtime", localtime);
            putExtra("dayOfTheWeek", dayOfTheWeek);
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            generateRequestCode(dayOfTheWeek, localtime),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // (already-exists / immutable)
        )

        _alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, // if going to wake up the device
            triggerTime,
            pendingIntent
        );
    }

    override fun cancelAlarm(item: AlarmItem) {
        val ( _, _, localtime, dayOfTheWeek ) = item;

        // TODO: THIS INTENT ARE GOING TO REFERENCE THE RECEIVER
        val intent = Intent();

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            generateRequestCode(dayOfTheWeek, localtime),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        );

        _alarmManager.cancel(pendingIntent);
    }

    private fun getNextTriggerTime(dayOfTheWeek: DayOfWeek, time: LocalTime): Long {
        // current datetime from the system clock
        val now = LocalDateTime.now();

        // Its going to return a target time between the current datetime and the time and day of the week provided
        val targetDateTime = LocalDateTime.of(now.toLocalDate(),time).with(dayOfTheWeek);

        return if (targetDateTime.isBefore(now)){
            targetDateTime
                .plusWeeks(1) // its going to return a long that represent the target time but next week (the target time pass)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }else{
            targetDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }
    }

    private fun generateRequestCode(dayOfWeek: DayOfWeek, time: LocalTime): Int {
        return dayOfWeek.value * 10000 + time.hour * 100 + time.minute;
    }
}