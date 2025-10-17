package com.example.classscheduler.domain.errors

import com.example.classscheduler.domain.primitives.Error

open class AuthError : Error(){
    object UserNotFound : AuthError();
    object InvalidCredentials : AuthError();

    object UnknownError : AuthError();
}