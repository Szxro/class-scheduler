package com.example.classscheduler.util.ext

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASSWORD_LENGTH = 8
private const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

/*
* Extensions methods for the String
* */
fun String.isValidEmail(): Boolean{
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches();
}

fun String.isPasswordValid(): Boolean{
    return this.isNotEmpty() &&
            this.length >= MIN_PASSWORD_LENGTH &&
            Pattern.compile(PASSWORD_PATTERN).matcher(this).matches();
}