package com.example.classscheduler.ui.resetpassword

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.classscheduler.R;
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.validation.validators.ResetPasswordValidator
import com.example.classscheduler.domain.interfaces.AuthRepository
import kotlinx.coroutines.launch

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): BaseViewModel<ResetPasswordIntent, ResetPasswordState>() {
    private val _state = MutableStateFlow(ResetPasswordState());

    val state = _state.asStateFlow();

    init {
        addValidator(ResetPasswordValidator())
    }

    override fun onIntent(intent: ResetPasswordIntent) {
        when(intent){
            is ResetPasswordIntent.OnEmailChange -> {
                _state.update { currentState -> currentState.copy(email = intent.email) }
            }
            ResetPasswordIntent.OnResetPasswordButtonClicked -> {
                val validationResult = validator!!.validate(_state.value);

                _state.update { currentState -> currentState.copy(
                    emailHasError = validationResult["email"]?.errorMessage
                )}

                if(!validationResult.values.all { it.isValid }) return;

                _state.update { currentState -> currentState.copy(isLoading = true) };

                viewModelScope.launch {
                    val result = authRepository.resetPassword(_state.value.email);

                    _state.update { currentState -> currentState.copy(isLoading = false) };

                    result.match(
                        onSuccess = {
                            channel.run {
                                send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.password_reset_sent)));
                                send(UiEvent.Navigate(Screen.SignIn));
                            }
                        },
                        onFailure = { error ->
                            channel.send(UiEvent.ShowSnackBar(error.message));
                        }
                    )
                }
            }
            ResetPasswordIntent.OnNavigateToSignIn -> {
                viewModelScope.launch {
                    channel.send(UiEvent.Navigate(Screen.SignIn));
                }
            }
        }
    }
}