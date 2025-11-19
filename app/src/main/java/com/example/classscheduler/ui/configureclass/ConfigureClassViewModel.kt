package com.example.classscheduler.ui.configureclass

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.ext.toLocalTime
import com.example.classscheduler.core.utils.helpers.getCurrentDay
import com.example.classscheduler.core.utils.validation.validators.ConfigureClassValidator
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import com.example.classscheduler.data.repository.ClassRepositoryImpl
import com.example.classscheduler.data.services.AlarmSchedulerServiceImpl
import com.example.classscheduler.domain.models.AlarmItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ConfigureClassViewModel @Inject constructor(
    private val classRepository: ClassRepositoryImpl,
    private val authRepository: AuthRepositoryImpl,
    private val alarmSchedulerService: AlarmSchedulerServiceImpl
) : BaseViewModel<ConfigureClassIntent, ConfigureClassState>() {
    private val _state = MutableStateFlow(ConfigureClassState());

    val state = _state.asStateFlow();

    init {
        addValidator(ConfigureClassValidator());
        loadClasses();
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onIntent(intent: ConfigureClassIntent) {
        when (intent) {
            is ConfigureClassIntent.OnSelectedClassChange -> {
                _state.update { currentState -> currentState.copy(selectedClass = intent.selectedClass) }
            }

            ConfigureClassIntent.OnConfigure -> {
                val validationResult = validator!!.validate(_state.value);

                _state.update { currentState ->
                    currentState.copy(
                        selectedClassHasError = validationResult["selected-class"]?.errorMessage
                    )
                }

                if (!validationResult.values.all { it.isValid }) return;

                viewModelScope.launch {
                    _state.update { currentState -> currentState.copy(isLoading = true) };

                    // Setting up the alarms
                    val className = _state.value.selectedClass!!.name;

                    val alarmItems = _state.value.selectedClass!!.schedule.map { schedule ->
                        AlarmItem(
                            title = "Class reminder: $className",
                            description = "Starts on ${schedule.day} at ${
                                schedule.startTimeLong!!.toLocalTime().format(
                                    DateTimeFormatter.ofPattern("h:mm a")
                                )
                            }",
                            localTime = schedule.startTimeLong.toLocalTime(),
                            dayOfWeek = getCurrentDay(schedule.day)
                        )
                    }

                    alarmItems.forEach { item ->  alarmSchedulerService.scheduleWeeklyAlarm(item) };

                    // updating the class item
                    val result = classRepository.update(_state.value.selectedClass!!.copy(configured = true));

                    result.match(
                        onSuccess = {
                            channel.apply {
                                send(UiEvent.ShowSnackBar(UiText.DynamicString("ALARM CONFIGURED!!!!")));
                                loadClasses();
                            }
                        },
                        onFailure = { error ->
                            channel.send(UiEvent.ShowSnackBar(error.message));
                        }
                    )

                    _state.update { currentState -> currentState.copy(isLoading = false) };
                }
            }
        }
    }

    private fun loadClasses(): Unit {
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true) };

            val result = classRepository.getClassesByOwnerId(authRepository.currentUser?.uid!!);

            result.match(
                onSuccess = { classes ->
                    _state.update { currentState -> currentState.copy(classes = classes!!) }
                },
                onFailure = { error ->
                    channel.send(UiEvent.ShowSnackBar(error.message))
                }
            )

            _state.update { currentState -> currentState.copy(isLoading = false) };
        }
    }
}