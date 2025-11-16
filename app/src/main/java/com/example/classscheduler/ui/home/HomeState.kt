package com.example.classscheduler.ui.home

import com.example.classscheduler.domain.models.Class
import com.google.firebase.auth.FirebaseUser

data class HomeState(
    val currentUser: FirebaseUser? = null,
    val currentClasses: List<Class> = emptyList(),
    val isLoading: Boolean = false
);
