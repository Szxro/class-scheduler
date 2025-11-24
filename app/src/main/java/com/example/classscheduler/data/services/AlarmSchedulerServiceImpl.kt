package com.example.classscheduler.data.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.classscheduler.data.receivers.WeeklyScheduleBroadCast
import com.example.classscheduler.domain.interfaces.AlarmSchedulerService
import com.example.classscheduler.domain.models.AlarmItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmSchedulerServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlarmSchedulerService {
    private val _alarmManager = context.getSystemService(AlarmManager::class.java);

    override fun scheduleWeeklyAlarm(item: AlarmItem) {
        val (title, description, localtime, dayOfTheWeek) = item;

        val triggerTime = getNextTriggerTime(dayOfTheWeek,localtime);

        val requestCode = generateRequestCode(dayOfTheWeek, localtime, title);

        val intent = Intent(context, WeeklyScheduleBroadCast::class.java).apply {
            putExtra("title", title);
            putExtra("description", description);
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // (already-exists / immutable)
        )

        _alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, // if going to wake up the device
            triggerTime,
            pendingIntent
        );
    }

    override fun cancelAlarm(item: AlarmItem) {
        val (title, _, localtime, dayOfTheWeek) = item;

        val requestCode = generateRequestCode(dayOfTheWeek, localtime, title);

        val intent = Intent(context, WeeklyScheduleBroadCast::class.java);

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        );

        _alarmManager.cancel(pendingIntent);
    }

    private fun getNextTriggerTime(dayOfWeek: DayOfWeek, time: LocalTime): Long {
        // getting the current date time
        val now = LocalDateTime.now()

        // getting the number value of the DayOfTheWeek ENUM
        val todayDow = now.dayOfWeek.value;
        val targetDow = dayOfWeek.value;

        // Calculate how many days ahead the target weekday is
        val daysUntilTarget = when {
            targetDow > todayDow -> targetDow - todayDow // The target date is LATER, within this same week.
            targetDow < todayDow -> 7 - (todayDow - targetDow) // The target day has already passed this week.
            else -> if (time.isAfter(now.toLocalTime())) 0 else 7 // today is the alarm day
        }

        val targetDate = now.toLocalDate().plusDays(daysUntilTarget.toLong());

        val targetDateTime = targetDate.atTime(time) // LocalDateTime (have date, month, year)

        return targetDateTime // Long -> to LocalTime (hour - minutes AM/PM)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    private fun generateRequestCode(dayOfWeek: DayOfWeek, time: LocalTime, title: String): Int {
        // Avoid collisions across classes with the same schedule
        return dayOfWeek.value * 10000 + time.hour * 100 + time.minute + title.hashCode();
    }
}