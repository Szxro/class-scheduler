package com.example.classscheduler.ui.createclass

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.domain.models.Schedule

data class CreateClassState(
    val isLoading: Boolean = false,
    val code: String = "",
    val codeHasError: UiText? = null,
    val name: String = "",
    val nameHasError: UiText? = null,
    val teacher: String = "",
    val teacherHasError: UiText? = null,
    val classroom: String = "",
    val classroomHasError: UiText? = null,
    val schedule : List<Schedule> = emptyList(),
    val scheduleHasError: UiText? = null,
);
