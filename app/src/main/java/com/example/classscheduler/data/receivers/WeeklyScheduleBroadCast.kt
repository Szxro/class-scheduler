package com.example.classscheduler.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.classscheduler.data.services.NotificationServiceImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint // can define more than one entry point
class WeeklyScheduleBroadCast : BroadcastReceiver() {
    @Inject
    lateinit var notificationService: NotificationServiceImpl

    override fun onReceive(context: Context, intent: Intent?): Unit {
        val title = intent?.getStringExtra("title") ?: return;
        val description = intent.getStringExtra("description") ?: return;

        // show notification
        notificationService.showNotification(title, description);
    }
}