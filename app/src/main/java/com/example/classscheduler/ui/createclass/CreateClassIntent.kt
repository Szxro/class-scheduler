package com.example.classscheduler.ui.createclass

import com.example.classscheduler.core.ui.UiIntent

sealed class CreateClassIntent : UiIntent {

    data class OnNameChange(val name: String) : CreateClassIntent();

    data class OnCodeChange(val code: String) : CreateClassIntent();

    data class OnTeacherChange(val teacher: String) : CreateClassIntent();

    data class OnClassRoomChange(val classRoom: String) : CreateClassIntent();

    data class OnDaysChange(val days: List<String>) : CreateClassIntent();

    data class OnScheduleChange(val day: String,val start: Long? , val end: Long?): CreateClassIntent();

    object OnSaveClicked : CreateClassIntent();

    object OnNavigateToManageClasses : CreateClassIntent();
}