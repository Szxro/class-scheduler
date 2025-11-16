package com.example.classscheduler.ui.deleteclass

import com.example.classscheduler.core.ui.UiIntent
import com.example.classscheduler.domain.models.Class

sealed class DeleteClassIntent : UiIntent {

    data class OnClassSelected(val selectedClass: Class?): DeleteClassIntent();

    object OnDeleteClass : DeleteClassIntent();

    object OnNavigateToManageClasses : DeleteClassIntent();
}