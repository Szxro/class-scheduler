package com.example.classscheduler.ui.home

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRemoteDataSource: AuthRepositoryImpl
) : BaseViewModel<HomeIntent, HomeState>() {
    private val _state = MutableStateFlow(HomeState());

    val state = _state.asStateFlow();

    init {
        _state.update { currentState -> currentState.copy(currentUser =  authRemoteDataSource.currentUser) }
    }

    override fun onIntent(intent: HomeIntent) {
        when(intent){
            HomeIntent.OnLogoOut -> {
                _state.update { currentState -> currentState.copy(isLoading = true) }

                viewModelScope.launch {
                    val result = authRemoteDataSource.signOut();

                    _state.update { currentState -> currentState.copy(isLoading = false) }

                    result.match(
                        onSuccess = {
                            channel.send(UiEvent.Navigate(Screen.SignIn));
                        },
                        onFailure = { error ->
                            channel.send(UiEvent.ShowSnackBar(error.message));
                        }
                    )
                }
            }
        }
    }
}