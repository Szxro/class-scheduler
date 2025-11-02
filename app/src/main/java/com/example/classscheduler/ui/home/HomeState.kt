package com.example.classscheduler.ui.home

import com.google.firebase.auth.FirebaseUser

data class HomeState(
    val currentUser: FirebaseUser? = null,
    val isLoading: Boolean = false
);
