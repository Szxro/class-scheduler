package com.example.classscheduler.core.utils.constants

import com.example.classscheduler.BuildConfig;

/**
 * Contains secrets-related constants used across the application.
 */
object SecretsConstants{

    /**
    * Web client ID used for authentication (e.g., Google Sign-In).
    *
    * This value is supplied by the build configuration to avoid
    * embedding sensitive data directly in the codebase.
    */
    const val WEB_CLIENT_ID = BuildConfig.WEB_CLIENT_ID;
}
