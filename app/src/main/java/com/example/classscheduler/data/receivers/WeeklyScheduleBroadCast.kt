package com.example.classscheduler.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.classscheduler.data.services.AlarmSchedulerServiceImpl
import com.example.classscheduler.data.services.NotificationServiceImpl
import com.example.classscheduler.domain.models.AlarmItem
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint // can define more than one entry point
class WeeklyScheduleBroadCast : BroadcastReceiver() {
    @Inject
    lateinit var alarmScheduler: AlarmSchedulerServiceImpl;

    @Inject
    lateinit var notificationService: NotificationServiceImpl

    override fun onReceive(context: Context, intent: Intent?): Unit {
        val title = intent?.getStringExtra("title") ?: return;
        val description = intent.getStringExtra("description") ?: return;

        val localtime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("localtime", LocalTime::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("localtime") as? LocalTime
        } ?: return;

        val dowValue = intent.getIntExtra("dayOfTheWeek", -1);

        if (dowValue !in 1..7) return

        // Obtaining a day of the week of the given value
        val dayOfTheWeek = DayOfWeek.of(dowValue)

        // show notification
        notificationService.showNotification(title, description);

        // Re-schedule the alarm for the next week
        val nextWeekAlarmItem = AlarmItem(
            title,
            description,
            localtime,
            dayOfTheWeek
        );

        alarmScheduler.scheduleWeeklyAlarm(nextWeekAlarmItem);
    }
}