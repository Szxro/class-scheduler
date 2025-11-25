package com.example.classscheduler.domain.models

import com.google.firebase.firestore.DocumentId

/**
 * Represents an academic class or course within the scheduler system.
 *
 * @property id The Firestore-generated document ID for the class. Annotated with [DocumentId].
 * @property name The display name of the class (e.g., “Physics II”).
 * @property code A short alphanumeric code identifying the class (e.g., “PHY-202”).
 * @property teacher The name of the instructor or professor responsible for the class.
 * @property classroom The physical or virtual location where the class is held.
 * @property ownerId The ID of the user who owns or created the class entry.
 * @property schedule A list of [Schedule] objects defining the time blocks for the class.
 * @property scheduleDays A list of weekday labels (e.g., “MONDAY”, “WEDNESDAY”) associated with the class.
 *                      This helps with quick filtering without parsing the full schedule.
 * @property configured Indicates whether the class has been fully configured
 */
data class Class(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val code: String = "",
    val teacher: String = "",
    val classroom: String = "",
    val ownerId: String = "",
    val schedule: List<Schedule> = emptyList(),
    val scheduleDays: List<String> = emptyList(),
    val configured: Boolean = false,
)
