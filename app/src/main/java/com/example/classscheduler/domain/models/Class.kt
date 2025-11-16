package com.example.classscheduler.domain.models

import com.google.firebase.firestore.DocumentId

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
