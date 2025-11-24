package com.example.classscheduler.ui.dayschedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.interfaces.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DayScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classRepository: ClassRepository,
    private val authRepositoryImpl: AuthRepository
) : BaseViewModel<DayScheduleIntent, DayScheduleState>() {
    private val _state = MutableStateFlow(DayScheduleState())

    val state = _state.asStateFlow();

    init {
        // With the savedStateHandle can access the route args (can inject it with hilt)
        val (day) = savedStateHandle.toRoute<DayScheduleRoute>();

        _state.update { currentState -> currentState.copy(day = day) }

        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true) }

            val result = classRepository.getClassesByDay(day, authRepositoryImpl.currentUser?.uid!!);

            result.match(
                onSuccess = { classes ->
                    _state.update { currentState -> currentState.copy(classes = classes!!) }
                },
                onFailure = { error ->
                    channel.send(UiEvent.ShowSnackBar(error.message));
                }
            )

            _state.update { currentState -> currentState.copy(isLoading = false) }
        }
    }

    override fun onIntent(intent: DayScheduleIntent) {
        when(intent){
            DayScheduleIntent.OnNavigateToHomeScreen -> {
                viewModelScope.launch {
                    channel.send(UiEvent.Navigate(Screen.Home));
                }
            }
        }
    }
}