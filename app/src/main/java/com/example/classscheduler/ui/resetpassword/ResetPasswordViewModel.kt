package com.example.classscheduler.ui.resetpassword

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.validateAll
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.blankOrNull
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.classscheduler.R;
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.constants.PatternConstants
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.validation.guards.pattern
import kotlinx.coroutines.launch

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl
): BaseViewModel<ResetPasswordIntent>() {
    private val _state = MutableStateFlow(ResetPasswordState());

    val state = _state.asStateFlow();

    override fun onIntent(intent: ResetPasswordIntent) {
        when(intent){
            is ResetPasswordIntent.OnEmailChange -> {
                _state.update { currentState -> currentState.copy(email = intent.email) }
            }
            ResetPasswordIntent.OnResetPasswordButtonClicked -> {
                if(!isFormValid()) return;

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
        }
    }

    private fun isFormValid(): Boolean{
        val emailValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = _state.value.email,
                parameterName = "email",
                message = UiText.StringResource(R.string.blank_input_error,"email")
            ),
            Guard.against.pattern(
                value = _state.value.email,
                parameterName = "email",
                pattern = PatternConstants.EMAIL_PATTERN,
                message = UiText.StringResource(R.string.invalid_email_error)
            )
        );

        _state.update { currentState -> currentState.copy(emailHasError = emailValidationResult.errorMessage) };

        return emailValidationResult.isValid;
    }
}