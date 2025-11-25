package com.example.classscheduler.domain.interfaces

import com.example.classscheduler.domain.models.AlarmItem

/**
 * Defines the contract for scheduling and managing weekly alarms in the app.
 *
 */
interface AlarmSchedulerService {
    fun scheduleWeeklyAlarm(item: AlarmItem);

    fun cancelAlarm(item: AlarmItem);
}