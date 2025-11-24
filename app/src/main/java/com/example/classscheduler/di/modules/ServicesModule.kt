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

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Provides
    fun alarmScheduler(
        @ApplicationContext context: Context
    ): AlarmSchedulerService{
        return AlarmSchedulerServiceImpl(context);
    };

    @Provides
    fun notificationService(
        @ApplicationContext context: Context
    ): NotificationService{
        return NotificationServiceImpl(context)
    }
}