package com.example.classscheduler.core.ui

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