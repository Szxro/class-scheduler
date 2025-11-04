package com.example.classscheduler.domain.models

import com.google.firebase.firestore.DocumentId

data class ClassItem(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val code: Int = 0,
    val teacher: String = "",
    val classroom: String = "",
    val days: List<String> = emptyList(),
    val hours: List<String> = emptyList()
)
