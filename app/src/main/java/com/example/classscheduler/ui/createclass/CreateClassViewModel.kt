package com.example.classscheduler.ui.createclass

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent.*
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.validation.validators.CreateClassValidator
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import com.example.classscheduler.data.repository.ClassRepositoryImpl
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.domain.models.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateClassViewModel @Inject constructor(
    private val classRepository: ClassRepositoryImpl,
    private val authRepositoryImpl: AuthRepositoryImpl
)
    : BaseViewModel<CreateClassIntent, CreateClassState>() {
    private val _state = MutableStateFlow(CreateClassState());

    val state = _state.asStateFlow();

    init {
        addValidator(CreateClassValidator());
    }

    override fun onIntent(intent: CreateClassIntent) {
        when(intent){
            is CreateClassIntent.OnNameChange -> {
                _state.update { currentState -> currentState.copy(name = intent.name) }
            }
            is CreateClassIntent.OnCodeChange -> {
                _state.update { currentState -> currentState.copy(code = intent.code) }
            }
            is CreateClassIntent.OnTeacherChange -> {
                _state.update { currentState -> currentState.copy(teacher = intent.teacher) }
            }
            is CreateClassIntent.OnClassRoomChange -> {
                _state.update { currentState -> currentState.copy(classroom = intent.classRoom) }
            }
            is CreateClassIntent.OnDaysChange ->{
                _state.update { currentState ->
                    val newSchedules = intent.days.map { day ->
                        // Is going to find the schedule base on the day else is going to create a new one
                        currentState.schedule.find { schedule -> schedule.day == day  } ?: Schedule(day)
                    };
                    currentState.copy( schedule = newSchedules )
                }
            }
            is  CreateClassIntent.OnScheduleChange -> {
                _state.update { currentState ->
                    // Update the schedule start and end time (base on the day selected)
                    val updatedSchedules = currentState.schedule.map { schedule ->
                        if (schedule.day == intent.day) {
                            schedule.copy(
                                startTimeLong = intent.start,
                                endTimeLong = intent.end
                            )
                        } else schedule
                    }
                    currentState.copy(schedule = updatedSchedules)
                }
            }
            CreateClassIntent.OnNavigateToManageClasses -> {
                viewModelScope.launch {
                    channel.send(Navigate(Screen.ManageClasses))
                }
            }
            CreateClassIntent.OnSaveClicked -> {
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

                if(!validationResult.values.all { it.isValid }) return;

                viewModelScope.launch {
                    _state.update { currentState -> currentState.copy(isLoading = true) }

                    val result = classRepository.create(Class(
                        code = _state.value.code,
                        name = _state.value.name,
                        teacher = _state.value.teacher,
                        classroom = _state.value.classroom,
                        ownerId = authRepositoryImpl.currentUser?.uid!!,
                        scheduleDays = _state.value.schedule.map { schedule -> schedule.day },
                        schedule = _state.value.schedule,
                    ));

                    result.match(
                        onSuccess = {
                            channel.apply {
                                send(ShowSnackBar(UiText.DynamicString("CLASS CREATED!!!")));

                                _state.value = CreateClassState() // Resetting the state
                            }
                        },
                        onFailure = { error ->
                            channel.send(ShowSnackBar(error.message))
                        }
                    );

                    _state.update { currentState -> currentState.copy(isLoading = false) }
                }
            }
        }
    }
}