package com.example.classscheduler.domain.interfaces

import com.example.classscheduler.domain.models.AlarmItem

interface AlarmSchedulerService {
    fun scheduleWeeklyAlarm(item: AlarmItem);

    fun cancelAlarm(item: AlarmItem);
}