package com.example.classscheduler.ui.updateclass

import com.example.classscheduler.core.ui.UiIntent
import com.example.classscheduler.domain.models.Class

sealed class UpdateClassIntent : UiIntent {
    object OnNavigateToManageClasses : UpdateClassIntent();

    object OnUpdateClass : UpdateClassIntent();

    data class OnSelectedClass(val selectedClass: Class?) : UpdateClassIntent();

    data class OnNameChange(val name: String) : UpdateClassIntent();

    data class OnCodeChange(val code: String) : UpdateClassIntent();

    data class OnTeacherChange(val teacher: String) : UpdateClassIntent();

    data class OnClassRoomChange(val classRoom: String) : UpdateClassIntent();

    data class OnDaysChange(val days: List<String>) : UpdateClassIntent();

    data class OnScheduleChange(val day: String,val start: Long? , val end: Long?): UpdateClassIntent();
}