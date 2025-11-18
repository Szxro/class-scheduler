package com.example.classscheduler.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.classscheduler.data.services.NotificationServiceImpl.Companion.CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClassSchedulerHiltApp : Application(){
    override fun onCreate() {
        super.onCreate()
        // when the app its created is going to create the notification channel
        createNotificationChannel();
    }
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Weekly Schedule",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = "This channel is used to display weekly alarms"

        val notificationManager = getSystemService(NotificationManager::class.java);

        notificationManager.createNotificationChannel(channel)
    }
}