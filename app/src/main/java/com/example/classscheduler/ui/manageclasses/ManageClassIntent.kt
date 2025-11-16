package com.example.classscheduler.ui.manageclasses

import com.example.classscheduler.core.ui.UiIntent

sealed class ManageClassIntent : UiIntent {

    object OnNavigateToHome : ManageClassIntent();

    object OnNavigateToCreateClass : ManageClassIntent();

    object OnNavigateToUpdateClass : ManageClassIntent();

    object OnNavigateToDeleteClass : ManageClassIntent();

    object OnNavigateToConfigureClass : ManageClassIntent();
}