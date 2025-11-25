package com.example.classscheduler.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.classscheduler.domain.interfaces.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * BroadcastReceiver responsible for handling weekly schedule notifications.
 *
 * This receiver is triggered by an external broadcast (e.g., AlarmManager)
 * and displays a notification using the injected [NotificationService].
 */
@AndroidEntryPoint // can define more than one entry point
class WeeklyScheduleBroadCast : BroadcastReceiver() {
    @Inject
    lateinit var notificationService: NotificationService

    /**
     * Called when the broadcast is received.
     *
     * Extracts the notification title and description from the intent and
     * displays the notification through [notificationService].
     *
     * If either `"title"` or `"description"` is missing, the method returns
     * early and no notification is shown.
     *
     * @param context The application or component context.
     * @param intent The intent that triggered this broadcast.
     */
    override fun onReceive(context: Context, intent: Intent?): Unit {
        val title = intent?.getStringExtra("title") ?: return;
        val description = intent.getStringExtra("description") ?: return;

        // show notification
        notificationService.showNotification(title, description);
    }
}