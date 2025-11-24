package com.example.classscheduler.ui.home

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiEvent.*
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.helpers.getCurrentDay
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.interfaces.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRemoteDataSource: AuthRepository,
    private val classRepository: ClassRepository
) : BaseViewModel<HomeIntent, HomeState>() {
    private val _state = MutableStateFlow(HomeState());

    val state = _state.asStateFlow();

    init {
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true) };

            val result = classRepository.getClassesByDay(day = getCurrentDay(), authRemoteDataSource.currentUser?.uid!!);

            result.match(
                onSuccess = { classes ->
                    _state.update { currentState -> currentState.copy(
                        currentUser = authRemoteDataSource.currentUser,
                        currentClasses = classes ?: emptyList()
                    ) }
                },
                onFailure = { error ->
                    channel.send(UiEvent.ShowSnackBar(error.message));
                }
            );

            _state.update { currentState -> currentState.copy(isLoading = false) };
        }
    }

    override fun onIntent(intent: HomeIntent) {
        when(intent){
            HomeIntent.OnManageClassesClicked ->{
                viewModelScope.launch {
                    channel.send(Navigate(Screen.ManageClasses))
                }
            }
            HomeIntent.OnLogoOut -> {
                _state.update { currentState -> currentState.copy(isLoading = true) }

                viewModelScope.launch {
                    val result = authRemoteDataSource.signOut();

                    _state.update { currentState -> currentState.copy(isLoading = false) }

                    result.match(
                        onSuccess = {
                            channel.send(Navigate(Screen.SignIn));
                        },
                        onFailure = { error ->
                            channel.send(ShowSnackBar(error.message));
                        }
                    )
                }
            }
            is HomeIntent.OnNavigateToDaySchedule -> {
                viewModelScope.launch {
                    channel.send(Navigate(Screen.DaySchedule, intent.day));
                }
            }
        }
    }
}