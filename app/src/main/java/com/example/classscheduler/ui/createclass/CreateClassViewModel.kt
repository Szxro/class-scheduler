package com.example.classscheduler.ui.createclass

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent.*
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.validation.validators.CreateClassValidator
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.domain.models.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.classscheduler.R;
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.interfaces.ClassRepository

@HiltViewModel
class CreateClassViewModel @Inject constructor(
    private val classRepository: ClassRepository,
    private val authRepositoryImpl: AuthRepository
) : BaseViewModel<CreateClassIntent, CreateClassState>() {
    private val _state = MutableStateFlow(CreateClassState());

    val state = _state.asStateFlow();

    init {
        addValidator(CreateClassValidator());
    }

    /**
     * Handles incoming CreateClassIntent actions and delegates them
     * to their corresponding handler functions.
     *
     * @param intent The intent representing a user action in the Create Class screen.
     */
    override fun onIntent(intent: CreateClassIntent) {
        when (intent) {
            is CreateClassIntent.OnNameChange -> updateName(intent.name)
            is CreateClassIntent.OnCodeChange -> updateCode(intent.code)
            is CreateClassIntent.OnTeacherChange -> updateTeacher(intent.teacher)
            is CreateClassIntent.OnClassRoomChange -> updateClassRoom(intent.classRoom)
            is CreateClassIntent.OnDaysChange -> updateDays(intent.days)
            is CreateClassIntent.OnScheduleChange -> updateSchedule(intent.day, intent.start, intent.end);
            CreateClassIntent.OnNavigateToManageClasses -> navigateToManageClasses()
            CreateClassIntent.OnSaveClicked -> onSave();
        }
    }

    /**
     * Updates the class name in the current UI state.
     *
     */
    private fun updateName(name: String): Unit {
        _state.update { currentState -> currentState.copy(name = name) }
    }

    /**
     * Updates the class code in the current UI state.
     *
     */
    private fun updateCode(code: String): Unit {
        _state.update { currentState -> currentState.copy(code = code) }
    }

    /**
     * Updates the class teacher in the current UI state.
     *
     */
    private fun updateTeacher(teacher: String): Unit {
        _state.update { currentState -> currentState.copy(teacher = teacher) }
    }

    /**
     * Updates the class classroom in the current UI state.
     *
     */
    private fun updateClassRoom(classRoom: String): Unit {
        _state.update { currentState -> currentState.copy(classroom = classRoom) }
    }

    /**
     * Updates the selected days and ensures each day has a corresponding [Schedule] object.
     * Existing schedules are preserved; missing schedules are created with default values.
     */
    private fun updateDays(days: List<String>): Unit {
        _state.update { currentState ->
            val newSchedules = days.map { day ->
                // Is going to find the schedule base on the day else is going to create a new one
                currentState.schedule.find { schedule -> schedule.day == day } ?: Schedule(day)
            };
            currentState.copy(schedule = newSchedules)
        }
    }

    /**
     * Updates the start/end time of the schedule associated with the given day.
     * Other schedules remain unchanged.
     */
    private fun updateSchedule(day: String, start: Long?, end: Long?): Unit {
        _state.update { currentState ->
            // Update the schedule start and end time (base on the day selected)
            val updatedSchedules = currentState.schedule.map { schedule ->
                if (schedule.day == day) {
                    schedule.copy(
                        startTimeLong = start,
                        endTimeLong = end
                    )
                } else schedule
            }
            currentState.copy(schedule = updatedSchedules)
        }
    }

    /**
     * Handle the navigation to the manage classes screen
     *
     */
    private fun navigateToManageClasses() {
        viewModelScope.launch {
            channel.send(Navigate(Screen.ManageClasses))
        }
    }

    /**
     * Validates the form, updates error fields, and attempts to persist the class in the repository.
     *
     * - If validation fails, the function only updates the UI errors.
     * - If validation succeeds:
     *   - Shows loading state.
     *   - Saves the class via [ClassRepository].
     *   - Emits snackbar events for success/failure.
     *   - Resets the UI state on success.
     *
     * This function runs inside a coroutine in [viewModelScope].
     */
    private fun onSave(): Unit {
        val validationResult = validator!!.validate(_state.value);

        _state.update { currentState ->
            currentState.copy(
                codeHasError = validationResult["code"]?.errorMessage,
                nameHasError = validationResult["name"]?.errorMessage,
                teacherHasError = validationResult["teacher"]?.errorMessage,
                classroomHasError = validationResult["classroom"]?.errorMessage,
                scheduleHasError = validationResult["schedule"]?.errorMessage,
            )
        };

        if (!validationResult.values.all { it.isValid }) return;

        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true) }

            val result = classRepository.create(
                Class(
                    code = _state.value.code,
                    name = _state.value.name,
                    teacher = _state.value.teacher,
                    classroom = _state.value.classroom,
                    ownerId = authRepositoryImpl.currentUser?.uid!!,
                    scheduleDays = _state.value.schedule.map { schedule -> schedule.day },
                    schedule = _state.value.schedule,
                )
            );

            result.match(
                onSuccess = {
                    channel.send(ShowSnackBar(UiText.StringResource(R.string.class_created)));

                    _state.update { CreateClassState() }
                },
                onFailure = { error ->
                    channel.send(ShowSnackBar(error.message))
                }
            );

            _state.update { currentState -> currentState.copy(isLoading = false) }
        }
    }
}