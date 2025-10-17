package com.example.classscheduler.core.utils.ext

import com.example.classscheduler.core.utils.validation.guards.GuardClause
import com.example.classscheduler.core.utils.validation.ValidationResult

fun GuardClause.validateAll(
    vararg results: ValidationResult,
): ValidationResult{
    for(result in results){
        if(!result.isValid) return result;
    }
    return ValidationResult(isValid = true);
}