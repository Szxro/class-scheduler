package com.example.classscheduler.core.utils.validation.validators

import com.example.classscheduler.R
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.validateAll
import com.example.classscheduler.core.utils.validation.ValidationResult
import com.example.classscheduler.core.utils.validation.Validator
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.blankOrNull
import com.example.classscheduler.core.utils.validation.guards.emptyOrNull
import com.example.classscheduler.core.utils.validation.guards.ensureAllValid
import com.example.classscheduler.core.utils.validation.guards.nullValue
import com.example.classscheduler.ui.updateclass.UpdateClassState

/**
 * Validator for the [UpdateClassState] screen.
 */
class UpdateClassValidator : Validator<UpdateClassState>() {
    override fun validate(value: UpdateClassState): Map<String, ValidationResult> {
        validations.clear();

        val selectedClassValidationResult = Guard.against.validateAll(
            Guard.against.nullValue(
                value = value.selectedClass,
                parameterName = "selected class",
                message = UiText.DynamicString("The selected class can't be null or empty")
            )
        );

        validations["selected-class"] = selectedClassValidationResult;

        if(!selectedClassValidationResult.isValid) return validations;

        val codeValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.selectedClass!!.code,
                parameterName = "code",
                UiText.StringResource(R.string.blank_input_error, "code")
            )
        );

        validations["code"] = codeValidationResult;

        val nameValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.selectedClass.name,
                parameterName = "name",
                UiText.StringResource(R.string.blank_input_error, "name")
            )
        );

        validations["name"] = nameValidationResult;

        val teacherValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.selectedClass.teacher,
                parameterName = "teacher",
                UiText.StringResource(R.string.blank_input_error, "teacher")
            )
        );

        validations["teacher"] = teacherValidationResult;

        val classRoomValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.selectedClass.classroom,
                parameterName = "classroom",
                UiText.StringResource(R.string.blank_input_error, "classroom")
            )
        );

        validations["classroom"] = classRoomValidationResult;

        val scheduleValidationResult = Guard.against.validateAll(
            Guard.against.emptyOrNull(
                value = value.selectedClass.schedule,
                parameterName = "schedule"
            ),
            Guard.against.ensureAllValid(
                items = value.selectedClass.schedule,
                parameterName = "schedule",
                predicate = { schedule -> schedule.startTimeLong != schedule.endTimeLong },
                message = UiText.StringResource(R.string.start_time_and_end_time_must_be_different)
            )
        );

        validations["schedule"] = scheduleValidationResult;

        return  validations;
    }
}