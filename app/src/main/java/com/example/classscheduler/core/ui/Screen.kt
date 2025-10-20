package com.example.classscheduler.core.ui

sealed class Screen {
    object Home : Screen();
    object SignUp : Screen();

    object SignIn : Screen();
}