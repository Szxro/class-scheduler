package com.example.classscheduler.core.utils.validation.validators

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.validateAll
import com.example.classscheduler.core.utils.validation.ValidationResult
import com.example.classscheduler.core.utils.validation.Validator
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.nullValue
import com.example.classscheduler.ui.deleteclass.DeleteClassState

class DeleteClassValidator : Validator<DeleteClassState>() {
    override fun validate(value: DeleteClassState): Map<String, ValidationResult> {
        validations.clear();

        val selectedClassValidationResult = Guard.against.validateAll(
            Guard.against.nullValue(
                value = value.selectedClass,
                parameterName = "selectedClass",
                message = UiText.DynamicString("The selected class can't be null or empty")
            )
        );

        validations["selected-class"] = selectedClassValidationResult;

        return validations;
    }
}