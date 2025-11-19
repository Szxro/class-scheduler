package com.example.classscheduler.ui.configureclass

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.domain.models.Class

data class ConfigureClassState(
    val isLoading: Boolean = false,
    val classes: List<Class> = emptyList(),
    val selectedClass: Class? = null,
    val selectedClassHasError: UiText? = null
)
