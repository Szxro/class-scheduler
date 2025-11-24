package com.example.classscheduler.core.utils.constants

/**
 * Contains patterns-related constants used across the application.
 */
object PatternConstants {
    /**
     * Password validation pattern.
     *
     * Requirements enforced:
     * - At least one digit.
     * - At least one lowercase letter.
     * - At least one uppercase letter.
     * - No whitespace characters.
     * - Minimum length of 8 characters.
     */
    const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    /**
     * Email validation pattern.
     *
     * Matches standard emails containing:
     * - Allowed characters before the '@' (letters, digits, '.', '_', '%', '+', '-').
     * - A valid domain name.
     * - A top-level domain with at least 2 alphabetic characters.
     */
    const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
}