package com.example.classscheduler.ui.signin

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.domain.errors.AuthError
import com.example.classscheduler.R;
import com.example.classscheduler.core.utils.validation.guards.Guard
import com.example.classscheduler.core.utils.validation.guards.blankOrNull
import com.example.classscheduler.core.utils.validation.guards.pattern
import com.example.classscheduler.core.utils.constants.PatternConstants
import com.example.classscheduler.core.utils.ext.validateAll
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
) : BaseViewModel<SignInIntent>() {
    private val _state = MutableStateFlow(SignInState());

    val state = _state.asStateFlow();

    override fun onIntent(intent: SignInIntent) {
        when (intent) {
            is SignInIntent.OnEmailChange -> {
                _state.update { currentState -> currentState.copy(email = intent.email) };
            }

            is SignInIntent.OnPasswordChange -> {
                _state.update { currentState -> currentState.copy(password = intent.password) }
            }

            SignInIntent.OnPasswordVisibilityChange -> {
                _state.update { currentState -> currentState.copy(isPasswordHidden = !_state.value.isPasswordHidden) }
            }

            is SignInIntent.OnSignUp -> intent.navigateToSignUp()

            is SignInIntent.OnSignIn -> {
                if (!isFormValid()) return;

                _state.update { currentState -> currentState.copy(isLoading = true) }

                viewModelScope.launch {
                    val result = authRepository.signIn(_state.value.email, _state.value.password);

                    _state.update { currentState -> currentState.copy(isLoading = false) };

                    result.match(
                        onSuccess = { user ->
                            channel.send(UiEvent.Navigate(user))
                        },
                        onFailure = { error ->
                            val message = when (error) {
                                is AuthError.UserNotFound -> UiText.StringResource(R.string.user_not_found_exception)
                                is AuthError.InvalidCredentials -> UiText.StringResource(R.string.invalid_user_credentials_exception)
                                else -> UiText.StringResource(R.string.generic_exception)
                            }
                            channel.send(UiEvent.ShowSnackBar(message));
                        }
                    )
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        val emailValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                _state.value.email,
                "email",
                UiText.StringResource(R.string.blank_email_error)
            ),
            Guard.against.pattern(
                _state.value.email,
                PatternConstants.EMAIL_PATTERN,
                "email",
                UiText.StringResource(R.string.invalid_email_error))
        );

        val passwordValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                _state.value.password,
                "password",
                UiText.StringResource(R.string.blank_password_error)
            )
        );
        _state.update {
            it.copy(
                emailError = emailValidationResult.errorMessage,
                passwordError = passwordValidationResult.errorMessage
            )
        }
        return emailValidationResult.isValid && passwordValidationResult.isValid;
    }
}