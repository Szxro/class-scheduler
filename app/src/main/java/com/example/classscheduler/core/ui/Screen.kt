package com.example.classscheduler.core.ui

/**
 * Defines all navigable screens within the application.
 *
 * Each screen is represented as a distinct object inside this sealed class,
 * ensuring exhaustive handling in navigation-related logic.
 */
sealed class Screen {
    object Home : Screen();
    object SignUp : Screen();

    object SignIn : Screen();

    object ManageClasses : Screen();

    object CreateClass : Screen();

    object UpdateClass : Screen();

    object DeleteClass : Screen();

    object ConfigureClass : Screen();

    object ResetPassword : Screen();

    object DaySchedule : Screen();
}