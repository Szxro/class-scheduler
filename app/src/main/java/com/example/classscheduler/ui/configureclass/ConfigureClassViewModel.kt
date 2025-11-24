package com.example.classscheduler.ui.configureclass

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiEvent.*
import com.example.classscheduler.core.ui.UiText.*
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.ext.toLocalTime
import com.example.classscheduler.core.utils.helpers.getCurrentDay
import com.example.classscheduler.core.utils.validation.validators.ConfigureClassValidator
import com.example.classscheduler.domain.models.AlarmItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import com.example.classscheduler.R;
import com.example.classscheduler.domain.interfaces.AlarmSchedulerService
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.interfaces.ClassRepository

@HiltViewModel
class ConfigureClassViewModel @Inject constructor(
    private val classRepository: ClassRepository,
    private val authRepository: AuthRepository,
    private val alarmSchedulerService: AlarmSchedulerService
) : BaseViewModel<ConfigureClassIntent, ConfigureClassState>() {
    private val _state = MutableStateFlow(ConfigureClassState());

    val state = _state.asStateFlow();

    init {
        addValidator(ConfigureClassValidator());
        loadClasses();
    }

    override fun onIntent(intent: ConfigureClassIntent) {
        when (intent) {
            ConfigureClassIntent.OnNavigateToManageClass -> {
                viewModelScope.launch {
                    channel.send(Navigate(Screen.ManageClasses));
                }
            }

            is ConfigureClassIntent.OnSelectedClassChange -> {
                _state.update { currentState -> currentState.copy(selectedClass = intent.selectedClass) }
            }

            ConfigureClassIntent.OnConfigure -> handleConfigure(isConfigured = true);

            ConfigureClassIntent.OnCancel -> handleConfigure(isConfigured = false);
        }
    }

    private fun handleConfigure(isConfigured: Boolean) {
        val validationResult = validator!!.validate(_state.value);

        _state.update { currentState ->
            currentState.copy(
                selectedClassHasError = validationResult["selected-class"]?.errorMessage
            )
        }

        if (!validationResult.values.all { it.isValid }) return;

        val alarmMessage = if (isConfigured) StringResource(R.string.alarm_configured) else StringResource(R.string.alarm_configured);

        val className = _state.value.selectedClass!!.name;

        val alarmItems = _state.value.selectedClass!!.schedule.map { schedule ->
            AlarmItem(
                title = "Class reminder: $className",
                description = "Starts on ${schedule.day} at ${
                    schedule.startTimeLong!!.toLocalTime().format(
                        DateTimeFormatter.ofPattern("h:mm a")
                    )
                }",
                localTime = schedule.startTimeLong.toLocalTime().minusMinutes(15),
                dayOfWeek = getCurrentDay(schedule.day)
            )
        }

        if(isConfigured){
            alarmItems.forEach{item -> alarmSchedulerService.scheduleWeeklyAlarm(item)}
        }else{
            alarmItems.forEach { item ->  alarmSchedulerService.cancelAlarm(item) }
        }

        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true) };

            val result = classRepository.update(_state.value.selectedClass!!.copy(configured = isConfigured));

            result.match(
                onSuccess = {
                    channel.send(ShowSnackBar(alarmMessage))

                    _state.update { currentState -> currentState.copy(selectedClass = null) }
                },
                onFailure = { error ->
                    channel.send(ShowSnackBar(error.message));
                }
            )

            _state.update { currentState -> currentState.copy(isLoading = false) };
        }
        loadClasses();
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