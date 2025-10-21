package com.example.classscheduler.domain.errors

import com.example.classscheduler.domain.primitives.Error

open class AuthError : Error(){

    // Firebase Auth
    object UserNotFound : AuthError();

    object InvalidEmailOrPassword : AuthError();

    object EmailCollision : AuthError();

    // Google OAUTH
    object CredentialError : AuthError();

    object CredentialsCancellation : AuthError();

    object NoCredentialsFound : AuthError();

    object CredentialsCleanUpError : AuthError();

    object UnknownCredentials : AuthError();

    // Generic Error
    object UnknownError : AuthError();
}