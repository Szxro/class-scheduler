package com.example.classscheduler.core.utils.ext

import androidx.navigation.NavHostController

/**
 * Navigates to the given [route] while clearing the entire back stack.
 *
 * This extension is typically used after authentication flows or when entering
 * a new "root" section of the app, ensuring that the user cannot navigate back
 * to previous screens using the system back button.
 *
 * Internally, this performs:
 * - `popUpTo(graph.id)` with `inclusive = true` to remove all destinations.
 * - `launchSingleTop = true` to avoid creating duplicates if the destination
 *   is already at the top of the back stack.
 *
 * @param route The destination to navigate to. Usually a typed navigation route
 * (e.g., `HomeRoute` or `SignInRoute`).
 */
fun NavHostController.navigateWithClearStack(route: Any) {
    navigate(route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

/**
 * Navigates to the given [route] using `launchSingleTop`, preventing creation
 * of multiple instances of the same destination on the back stack.
 *
 * @param route The destination to navigate to.
 */
fun NavHostController.navigateSingleTop(route: Any) {
    navigate(route) { launchSingleTop = true }
}