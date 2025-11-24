package com.example.classscheduler.core.utils.validation.validators

import com.example.classscheduler.R
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.constants.PatternConstants
import com.example.classscheduler.core.utils.ext.validateAll
import com.example.classscheduler.core.utils.validation.ValidationResult
import com.example.classscheduler.core.utils.validation.Validator
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.blankOrNull
import com.example.classscheduler.core.utils.validation.guards.equal
import com.example.classscheduler.core.utils.validation.guards.pattern
import com.example.classscheduler.core.utils.validation.guards.stringToShort
import com.example.classscheduler.ui.signup.SignUpState

/**
 * Validator for the [SignUpState] screen.
 */
class SignUpValidator : Validator<SignUpState>() {
    override fun validate(value: SignUpState): Map<String, ValidationResult> {
        validations.clear();

        val emailValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.email,
                parameterName = "email",
                message = UiText.StringResource(R.string.blank_input_error,"email")
            ),
            Guard.against.pattern(
                value = value.email,
                pattern = PatternConstants.EMAIL_PATTERN,
                parameterName = "email",
                message = UiText.StringResource(R.string.invalid_email_error)
            )
        );

        validations["email"] = emailValidationResult;

        val passwordValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value.password,
                parameterName = "password",
                message = UiText.StringResource(R.string.blank_input_error,"password")
            ),
            Guard.against.stringToShort(
                value.password,
                minLength = 8,
                parameterName = "password",
                message = UiText.StringResource(R.string.invalid_input_length_error,"password",8)
            ),
            Guard.against.pattern(
                value.password,
                pattern = PatternConstants.PASSWORD_PATTERN,
                parameterName = "password",
                message = UiText.StringResource(R.string.invalid_password_error, 8)
            )
        );

        validations["password"] = passwordValidationResult;

        val confirmPasswordValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.confirmPassword,
                parameterName = "confirm password",
                message = UiText.StringResource(R.string.blank_input_error,"confirm password")
            ),
            Guard.against.equal(
                value1 = value.password,
                value2 = value.confirmPassword,
                parameterName1 = "password",
                parameterName2 = "confirm password",
                UiText.StringResource(R.string.inputs_must_match,"confirm password", "password")
            )
        );

        validations["confirm_password"] = confirmPasswordValidationResult;

        return validations;
    }
}