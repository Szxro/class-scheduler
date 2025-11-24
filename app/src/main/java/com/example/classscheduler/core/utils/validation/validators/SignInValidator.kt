package com.example.classscheduler.core.utils.validation.validators

import com.example.classscheduler.R
import com.example.classscheduler.core.ui.UiText.StringResource
import com.example.classscheduler.core.utils.constants.PatternConstants
import com.example.classscheduler.core.utils.ext.validateAll
import com.example.classscheduler.core.utils.validation.ValidationResult
import com.example.classscheduler.core.utils.validation.Validator
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.blankOrNull
import com.example.classscheduler.core.utils.validation.guards.pattern
import com.example.classscheduler.ui.signin.SignInState

/**
 * Validator for the [SignInState] screen.
 */
class SignInValidator : Validator<SignInState>() {
    override fun validate(value: SignInState): Map<String, ValidationResult> {
        validations.clear();

        val emailValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value.email,
                "email",
                StringResource(R.string.blank_input_error,"email")
            ),
            Guard.against.pattern(
                value.email,
                PatternConstants.EMAIL_PATTERN,
                "email",
                StringResource(R.string.invalid_email_error)
            )
        );

        validations["email"] = emailValidationResult;

        val passwordValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value.password,
                "password",
                StringResource(R.string.blank_input_error,"password")
            )
        );

        validations["password"] = passwordValidationResult;

        return validations;
    }
}