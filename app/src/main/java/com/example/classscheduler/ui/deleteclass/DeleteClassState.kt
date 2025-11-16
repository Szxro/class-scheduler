package com.example.classscheduler.ui.deleteclass

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.domain.models.Class

data class DeleteClassState(
    val isLoading: Boolean = false,
    val classes: List<Class>? = null,
    val selectedClass: Class? = null,
    val selectedClassHasError: UiText? = null
)