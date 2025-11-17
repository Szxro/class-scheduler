package com.example.classscheduler.ui.updateclass

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent.*
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.validation.validators.UpdateClassValidator
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import com.example.classscheduler.data.repository.ClassRepositoryImpl
import com.example.classscheduler.domain.models.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UpdateClassViewModel @Inject constructor(
    private val classRepository: ClassRepositoryImpl,
    private val authRepository: AuthRepositoryImpl
): BaseViewModel<UpdateClassIntent, UpdateClassState>(){
    private val _state = MutableStateFlow(UpdateClassState());

    val state = _state.asStateFlow();

    init {
        addValidator(UpdateClassValidator());
        loadClasses();
    }

    override fun onIntent(intent: UpdateClassIntent) {
        when(intent) {
            UpdateClassIntent.OnNavigateToManageClasses -> {
                viewModelScope.launch {
                    channel.send(Navigate(Screen.ManageClasses));
                }
            }

            is UpdateClassIntent.OnSelectedClass -> {
                _state.update { currentState -> currentState.copy(selectedClass = intent.selectedClass, selectedClassHasError = null) }
            }

            is UpdateClassIntent.OnNameChange ->{
                _state.update { currentState -> currentState.copy(selectedClass = _state.value.selectedClass?.copy(name = intent.name)) }
            }

            is UpdateClassIntent.OnClassRoomChange -> {
                _state.update { currentState -> currentState.copy(selectedClass = _state.value.selectedClass?.copy(classroom = intent.classRoom)) }
            }

            is UpdateClassIntent.OnCodeChange -> {
                _state.update { currentState -> currentState.copy(selectedClass = _state.value.selectedClass?.copy(code = intent.code)) }
            }

            is UpdateClassIntent.OnTeacherChange -> {
                _state.update { currentState -> currentState.copy(selectedClass = _state.value.selectedClass?.copy(teacher = intent.teacher)) }
            }

            is UpdateClassIntent.OnDaysChange -> {
                _state.update { currentState ->
                    val newSchedules = intent.days.map { day ->
                        currentState.selectedClass?.schedule?.find { schedule -> schedule.day == day } ?: Schedule(day)
                    };

                    currentState.copy( selectedClass = _state.value.selectedClass?.copy(schedule = newSchedules));
                }
            }
            is UpdateClassIntent.OnScheduleChange -> {
                _state.update { currentState ->
                    val updatedSchedules = currentState.selectedClass?.schedule?.map { schedule ->
                        if (schedule.day == intent.day) {
                            schedule.copy(
                                startTimeLong = intent.start,
                                endTimeLong = intent.end
                            )
                        } else schedule
                    }
                    currentState.copy(selectedClass = _state.value.selectedClass?.copy(schedule = updatedSchedules!!))
                }
            }
            UpdateClassIntent.OnUpdateClass -> {
                val validationResult = validator!!.validate(_state.value);

                _state.update { currentState ->
                    currentState.copy(
                        codeHasError = validationResult["code"]?.errorMessage,
                        nameHasError = validationResult["name"]?.errorMessage,
                        teacherHasError = validationResult["teacher"]?.errorMessage,
                        classroomHasError = validationResult["classroom"]?.errorMessage,
                        scheduleHasError = validationResult["schedule"]?.errorMessage,
                        selectedClassHasError = validationResult["selected-class"]?.errorMessage
                    )
                };

                if(!validationResult.values.all { it.isValid }) return;

                viewModelScope.launch {
                    _state.update { currentState -> currentState.copy(isLoading = true) }

                    val result = classRepository.update(_state.value.selectedClass!!);

                    result.match(
                        onSuccess = {
                            channel.apply {
                                send(ShowSnackBar(UiText.DynamicString("CLASS UPDATED!!!")));
                                _state.update { currentState -> currentState.copy(selectedClass = null) }
                                loadClasses();
                            }
                        },
                        onFailure = { error ->
                            channel.send(ShowSnackBar(error.message));
                        }
                    )

                    _state.update { currentState -> currentState.copy(isLoading = false) }
                }
            }
        }
    }

    private fun loadClasses():Unit{
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true) }

            val result = classRepository.getClassesByOwnerId(authRepository.currentUser?.uid!!);

            result.match(
                onSuccess = { classes ->
                    _state.update { currentState -> currentState.copy(classes = classes!!) }
                },
                onFailure = { error ->
                    channel.send(ShowSnackBar(error.message));
                }
            )

            _state.update { currentState -> currentState.copy(isLoading = false) }
        }
    }
}