package com.example.classscheduler.domain.primitives

import com.example.classscheduler.core.ui.UiText

/**
 * Base class for all domain-specific errors in the application.
 *
 * @property message The [UiText] describing the error in a user-friendly way.
 */
abstract class Error(val message: UiText);