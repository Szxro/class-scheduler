package com.example.classscheduler.core.utils.validation.guards

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.validation.ValidationResult
import java.util.regex.Pattern

fun GuardClause.pattern(
    value: String?,
    pattern: String,
    parameterName: String,
    message: UiText? = null
): ValidationResult{
    val result = Guard.against.blankOrNull(value,parameterName);

    if(!result.isValid) return result;

    val trimValue = value!!.trim();

    if(!Pattern.compile(pattern).matcher(trimValue).matches()){
        return ValidationResult(
            isValid = false,
            errorMessage = message ?: UiText.DynamicString("The $parameterName is invalid")
        );
    }

    return ValidationResult(isValid = true);
}