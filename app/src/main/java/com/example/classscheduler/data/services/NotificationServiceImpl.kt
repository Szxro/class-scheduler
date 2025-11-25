package com.example.classscheduler.data.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.classscheduler.MainActivity
import com.example.classscheduler.domain.interfaces.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Implementation of [NotificationService] responsible for
 * displaying system notifications related to class scheduling.
 *
 * @property context Application context used to build notifications and intents.
 */
class NotificationServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : NotificationService {
    private val _notificationManager = context.getSystemService(NotificationManager::class.java);

    /**
     * Displays a notification with the provided [title] and [description].
     *
     * The notification uses the [CHANNEL_ID] notification channel and
     * opens [MainActivity] when the user taps it.
     *
     * A unique notification ID is generated using the current system time to
     * prevent overwriting previous notifications.
     *
     * @param title The title of the notification.
     * @param description The message or body text of the notification.
     */
    override fun showNotification(title: String, description: String) {
        // Open the app when tapping the notification
        val intent = Intent(context, MainActivity::class.java).apply {
            flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        };

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context,CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build();

        val notificationId = System.currentTimeMillis().toInt();

        _notificationManager.notify(notificationId,notification);
    }

    companion object{
        const val CHANNEL_ID = "WeeklySchedule"
    }
}