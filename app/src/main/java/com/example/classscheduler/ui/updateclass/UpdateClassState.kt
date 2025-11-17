package com.example.classscheduler.ui.updateclass

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.domain.models.Class

data class UpdateClassState(
    val isLoading: Boolean = false,
    val classes: List<Class> = emptyList(),
    val selectedClass: Class? = null,
    val selectedClassHasError: UiText? = null,
    val codeHasError: UiText? = null,
    val nameHasError: UiText? = null,
    val teacherHasError: UiText? = null,
    val classroomHasError: UiText? = null,
    val scheduleHasError: UiText? = null,
)
