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

/**
 * Implementation of [AlarmSchedulerService] that schedules and cancels
 * weekly alarms using Android's [AlarmManager].
 *
 * @property context Application-level context injected by Hilt.
 */
@Singleton
class AlarmSchedulerServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlarmSchedulerService {
    private val _alarmManager = context.getSystemService(AlarmManager::class.java);

    /**
     * Schedules a weekly repeating alarm.
     *
     * @param item The alarm configuration including title, description, time,
     * and day of the week.
     */
    override fun scheduleWeeklyAlarm(item: AlarmItem) {
        val (title, description, localtime, dayOfTheWeek) = item;

        val triggerTime = getNextTriggerTime(dayOfTheWeek,localtime);

        val requestCode = generateRequestCode(dayOfTheWeek, localtime);

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

    /**
     * Cancels a previously scheduled weekly alarm for the supplied [AlarmItem].
     * The correct alarm is identified by regenerating the request code.
     *
     * @param item The alarm to cancel.
     */
    override fun cancelAlarm(item: AlarmItem) {
        val (_, _, localtime, dayOfTheWeek) = item;

        val requestCode = generateRequestCode(dayOfTheWeek, localtime);

        val intent = Intent(context, WeeklyScheduleBroadCast::class.java);

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        );

        _alarmManager.cancel(pendingIntent);
    }

    /**
     * Calculates the next absolute trigger time (in epoch milliseconds) for
     * an alarm on the given day of week and time.
     *
     * - If the day is later in the current week, it schedules for this week.
     * - If the day has already passed, it schedules for the next week.
     * - If the day is today, it checks whether the time is still upcoming.
     *
     * @param dayOfWeek The day of the week the alarm should recur.
     * @param time The local time when the alarm should fire.
     * @return The next trigger time in epoch milliseconds.
     */
    private fun getNextTriggerTime(dayOfWeek: DayOfWeek, time: LocalTime): Long {
        val now = LocalDateTime.now()

        val todayDow = now.dayOfWeek.value;
        val targetDow = dayOfWeek.value;

        val daysUntilTarget = when {
            targetDow > todayDow -> targetDow - todayDow
            targetDow < todayDow -> 7 - (todayDow - targetDow)
            else -> if (time.isAfter(now.toLocalTime())) 0 else 7
        }

        val targetDate = now.toLocalDate().plusDays(daysUntilTarget.toLong());

        val targetDateTime = targetDate.atTime(time)

        return targetDateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    /**
     * Generates a unique request code for the alarm. This helps prevent
     * collisions between different alarms that might share the same day/time.
     *
     *
     * @param dayOfWeek The scheduled day of the alarm.
     * @param time The time of day.
     * @return A unique integer code for the PendingIntent.
     */
    private fun generateRequestCode(dayOfWeek: DayOfWeek, time: LocalTime): Int {
        return dayOfWeek.value * 10000 + time.hour * 100 + time.minute;
    }
}