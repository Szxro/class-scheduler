package com.example.classscheduler.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.classscheduler.data.services.NotificationServiceImpl.Companion.CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the ClassScheduler app.
 *
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation and
 * establish the dependency injection container at the application level.
 *
 * When the application is created, this class ensures that the notification
 * channel required for weekly schedule alerts is registered with the system.
 */
@HiltAndroidApp
class ClassSchedulerHiltApp : Application(){
    override fun onCreate() {
        super.onCreate()
        // Create the notification channel on startup
        createNotificationChannel();
    }

    /**
     * Creates the notification channel used for class reminder notifications.
     *
     */
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