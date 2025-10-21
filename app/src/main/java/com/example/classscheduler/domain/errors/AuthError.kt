package com.example.classscheduler.domain.errors

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.domain.primitives.Error
import com.example.classscheduler.R;

open class AuthError(val error: UiText) : Error(error){

    // Firebase Auth
    object UserNotFound : AuthError(UiText.StringResource(R.string.user_not_found_exception));

    object InvalidEmailOrPassword : AuthError(UiText.StringResource(R.string.invalid_user_email_or_password_exception));

    object EmailCollision : AuthError(UiText.StringResource(R.string.email_collision_exception));

    // Google OAUTH

    object CredentialsCancellation : AuthError(UiText.StringResource(R.string.credentials_cancellation_exception));

    object NoCredentialsFound : AuthError(UiText.StringResource(R.string.no_credentials_found_exception));

    object CredentialsCleanUpError : AuthError(UiText.StringResource(R.string.credentials_clean_up_exception));

    object UnknownCredentials : AuthError(UiText.StringResource(R.string.unknown_credentials_exception));

    // Generic Error
    object UnknownError : AuthError(UiText.StringResource(R.string.generic_exception));
}