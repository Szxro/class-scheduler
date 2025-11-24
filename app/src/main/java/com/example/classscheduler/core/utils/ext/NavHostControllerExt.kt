package com.example.classscheduler.core.utils.ext

import androidx.navigation.NavHostController

fun NavHostController.navigateWithClearStack(route: Any) {
    navigate(route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}


fun NavHostController.navigateSingleTop(route: Any) {
    navigate(route) { launchSingleTop = true }
}