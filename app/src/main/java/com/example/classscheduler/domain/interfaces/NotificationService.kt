package com.example.classscheduler.domain.interfaces

/**
 * Defines the contract for displaying system notifications to the user.
 *
 */
interface NotificationService {
    fun showNotification(title: String, description: String);
}