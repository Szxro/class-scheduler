package com.example.classscheduler.di.modules

import android.content.Context
import com.example.classscheduler.data.services.AlarmSchedulerServiceImpl
import com.example.classscheduler.data.services.NotificationServiceImpl
import com.example.classscheduler.domain.interfaces.AlarmSchedulerService
import com.example.classscheduler.domain.interfaces.NotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module responsible for providing application-wide service
 * implementations such as alarm scheduling and notifications.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    /**
     * Provides an implementation of [AlarmSchedulerService] capable of
     * creating and canceling weekly scheduled alarms using [android.app.AlarmManager].
     *
     * @param context The application context used for system service access.
     * @return The concrete [AlarmSchedulerService] implementation.
     */
    @Provides
    fun alarmScheduler(
        @ApplicationContext context: Context
    ): AlarmSchedulerService{
        return AlarmSchedulerServiceImpl(context);
    };


    /**
     * Provides an implementation of [NotificationService] used to display
     * system notifications for scheduled class reminders.
     *
     * @param context The application context required for NotificationManager operations.
     * @return The concrete [NotificationService] implementation.
     */
    @Provides
    fun notificationService(
        @ApplicationContext context: Context
    ): NotificationService{
        return NotificationServiceImpl(context)
    }
}