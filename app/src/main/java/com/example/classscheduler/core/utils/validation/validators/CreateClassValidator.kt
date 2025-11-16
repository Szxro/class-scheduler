package com.example.classscheduler.core.utils.validation.validators

import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.validateAll
import com.example.classscheduler.core.utils.validation.ValidationResult
import com.example.classscheduler.core.utils.validation.Validator
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.blankOrNull
import com.example.classscheduler.ui.createclass.CreateClassState
import com.example.classscheduler.R;
import com.example.classscheduler.core.utils.validation.guards.emptyOrNull
import com.example.classscheduler.core.utils.validation.guards.ensureAllValid

class CreateClassValidator : Validator<CreateClassState>() {
    override fun validate(value: CreateClassState): Map<String, ValidationResult> {
        validations.clear();

        val codeValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.code,
                parameterName = "code",
                UiText.StringResource(R.string.blank_input_error, "code")
            )
        );

        validations["code"] = codeValidationResult;

        val nameValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.name,
                parameterName = "name",
                UiText.StringResource(R.string.blank_input_error, "name")
            )
        );

        validations["name"] = nameValidationResult;

        val teacherValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.teacher,
                parameterName = "teacher",
                UiText.StringResource(R.string.blank_input_error, "teacher")
            )
        );

        validations["teacher"] = teacherValidationResult;

        val classRoomValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = value.classroom,
                parameterName = "classroom",
                UiText.StringResource(R.string.blank_input_error, "classroom")
            )
        );

        validations["classroom"] = classRoomValidationResult;

        val scheduleValidationResult = Guard.against.validateAll(
            Guard.against.emptyOrNull(
                value = value.schedule,
                parameterName = "schedule"
            ),
            Guard.against.ensureAllValid(
                items = value.schedule,
                parameterName = "schedule",
                predicate = { schedule -> schedule.startTimeLong != schedule.endTimeLong },
                message = UiText.StringResource(R.string.start_time_and_end_time_must_be_different)
            )
        );

        validations["schedule"] = scheduleValidationResult;

        return validations;
    }
}