package com.example.classscheduler.ui.configureclass

import com.example.classscheduler.core.ui.UiIntent
import com.example.classscheduler.domain.models.Class

sealed class ConfigureClassIntent : UiIntent {
    data class OnSelectedClassChange(val selectedClass: Class) : ConfigureClassIntent();

    object OnConfigure : ConfigureClassIntent();

    object OnCancel : ConfigureClassIntent();

    object OnNavigateToManageClass : ConfigureClassIntent();
}