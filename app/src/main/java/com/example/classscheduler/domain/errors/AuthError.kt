package com.example.classscheduler.domain.errors

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.domain.primitives.Error
import com.example.classscheduler.R;

/**
 * Represents all authentication-related domain errors that can occur during
 * email/password or Google Sign-In operations.
 *
 */
sealed class AuthError(val error: UiText) : Error(error){

    /**
     * Error indicating that the provided user does not exist in Firebase.
     */
    object UserNotFound : AuthError(UiText.StringResource(R.string.user_not_found_exception));

    /**
     * Error indicating that either the email or password is incorrect.
     */
    object InvalidEmailOrPassword : AuthError(UiText.StringResource(R.string.invalid_user_email_or_password_exception));

    /**
     * Error indicating that the provided email address is already associated
     * with an existing Firebase account.
     */
    object EmailCollision : AuthError(UiText.StringResource(R.string.email_collision_exception));

    /**
     * Error indicating that the user has not yet verified their email address.
     */
    object EmailIsNotVerified : AuthError(UiText.StringResource(R.string.email_not_verified))

    /**
     * Error indicating that the user canceled the Google credential request flow.
     */
    object CredentialsCancellation : AuthError(UiText.StringResource(R.string.credentials_cancellation_exception));

    /**
     * Error indicating that no saved credentials were found for the user.
     */
    object NoCredentialsFound : AuthError(UiText.StringResource(R.string.no_credentials_found_exception));

    /**
     * Error indicating that a problem occurred while clearing stored credentials.
     */
    object CredentialsCleanUpError : AuthError(UiText.StringResource(R.string.credentials_clean_up_exception));

    /**
     * Error representing an unknown or unexpected issue with Google OAuth.
     */
    object UnknownCredentials : AuthError(UiText.StringResource(R.string.unknown_credentials_exception));
}