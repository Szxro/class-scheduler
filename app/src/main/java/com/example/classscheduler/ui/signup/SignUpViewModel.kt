package com.example.classscheduler.ui.signup

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.validateAll
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.blankOrNull
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.classscheduler.R;
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiEvent.ShowSnackBar
import com.example.classscheduler.core.ui.UiText.DynamicString
import com.example.classscheduler.core.ui.UiText.StringResource
import com.example.classscheduler.core.utils.constants.PatternConstants
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.validation.guards.equal
import com.example.classscheduler.core.utils.validation.guards.pattern
import com.example.classscheduler.core.utils.validation.guards.stringToShort
import com.example.classscheduler.domain.errors.AuthError
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: AuthRepositoryImpl
) : BaseViewModel<SignUpIntent>() {
    private val _state = MutableStateFlow(SignUpState());

    val state = _state.asStateFlow();

    override fun onIntent(intent: SignUpIntent): Unit {
        when(intent) {
            is SignUpIntent.OnEmailChange -> {
                _state.update { currentState -> currentState.copy(email = intent.email) }
            }

            is SignUpIntent.OnPasswordChange -> {
                _state.update { currentState -> currentState.copy(password = intent.password) }
            }

            is SignUpIntent.OnConfirmPasswordChange -> {
                _state.update { currentState -> currentState.copy(confirmPassword = intent.confirmPassword) }
            }

            SignUpIntent.OnPasswordVisibilityChange -> {
                _state.update { currentState -> currentState.copy(isPasswordHidden = !_state.value.isPasswordHidden) }
            }

            SignUpIntent.OnNavigateToSignIn ->{
                viewModelScope.launch {
                    channel.send(UiEvent.Navigate(Screen.SignIn))
                }
            }

            SignUpIntent.OnSignUp -> {
                if(!isFormValid()) return;

                _state.update { currentState -> currentState.copy(isLoading = true) }

                viewModelScope.launch {
                    val result = auth.signUpWithEmailAndPassword(_state.value.email,_state.value.password);

                    _state.update { currentState -> currentState.copy(isLoading = false) }

                    result.match(
                        onSuccess = {
                            channel.run {
                                send(ShowSnackBar(StringResource(R.string.email_verification_sent)));
                                send(UiEvent.Navigate(Screen.SignIn));
                            };
                        },
                        onFailure = { error ->
                            channel.send(ShowSnackBar(error.message));
                        }
                    )
                }
            }

            is SignUpIntent.OnSignUpWithGoogle -> {
                viewModelScope.launch {
                    val result = auth.signInWithGoogle(intent.context);

                    result.match(
                        onSuccess = { user ->
                            channel.send(UiEvent.Navigate(Screen.Home, user));
                        },
                        onFailure = { error ->
                            channel.send(ShowSnackBar(error.message));
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
                pattern = PatternConstants.EMAIL_PATTERN,
                parameterName = "email",
                message = UiText.StringResource(R.string.invalid_email_error)
            )
        );

        val passwordValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                _state.value.password,
                parameterName = "password",
                message = UiText.StringResource(R.string.blank_input_error,"password")
            ),
            Guard.against.stringToShort(
                _state.value.password,
                minLength = 8,
                parameterName = "password",
                message = UiText.StringResource(R.string.invalid_input_length_error,"password",8)
            ),
            Guard.against.pattern(
                _state.value.password,
                pattern = PatternConstants.PASSWORD_PATTERN,
                parameterName = "password",
                message = UiText.StringResource(R.string.invalid_password_error, 8)
            )
        );

        val confirmPasswordValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                value = _state.value.confirmPassword,
                parameterName = "confirm password",
                message = UiText.StringResource(R.string.blank_input_error,"confirm password")
            ),
            Guard.against.equal(
                value1 = _state.value.password,
                value2 = _state.value.confirmPassword,
                parameterName1 = "password",
                parameterName2 = "confirm password",
                UiText.StringResource(R.string.inputs_must_match,"confirm password", "password")
            )
        );

        _state.update { currentState -> currentState.copy(
            emailHasError = emailValidationResult.errorMessage,
            passwordHasError = passwordValidationResult.errorMessage,
            confirmPasswordHasError = confirmPasswordValidationResult.errorMessage
        ) }

        return emailValidationResult.isValid && passwordValidationResult.isValid && confirmPasswordValidationResult.isValid;
    }
}