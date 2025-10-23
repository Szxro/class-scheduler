package com.example.classscheduler.core.utils.validation.validators

import com.example.classscheduler.R
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.constants.PatternConstants
import com.example.classscheduler.core.utils.ext.validateAll
import com.example.classscheduler.core.utils.validation.ValidationResult
import com.example.classscheduler.core.utils.validation.Validator
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.blankOrNull
import com.example.classscheduler.core.utils.validation.guards.pattern
import com.example.classscheduler.ui.resetpassword.ResetPasswordState

class ResetPasswordValidator : Validator<ResetPasswordState>() {
    override fun validate(value: ResetPasswordState): Map<String, ValidationResult> {
        validations.clear();

        val emailValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.email,
                parameterName = "email",
                message = UiText.StringResource(R.string.blank_input_error,"email")
            ),
            Guard.against.pattern(
                value = value.email,
                parameterName = "email",
                pattern = PatternConstants.EMAIL_PATTERN,
                message = UiText.StringResource(R.string.invalid_email_error)
            )
        );

        validations["email"] = emailValidationResult;

        return  validations;
    }
}