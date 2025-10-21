package com.example.classscheduler.ui.signin

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.domain.errors.AuthError
import com.example.classscheduler.R;
import com.example.classscheduler.core.ui.UiEvent.*
import com.example.classscheduler.core.ui.UiText.*
import com.example.classscheduler.ui.signin.SignInIntent
import com.example.classscheduler.core.ui.Screen
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

            SignInIntent.OnNavigateToSignUp -> {
                viewModelScope.launch {
                    channel.send(Navigate(Screen.SignUp));
                }
            }

            is SignInIntent.OnSignInWithEmailAndPassword -> {
                if (!isFormValid()) return;

                _state.update { currentState -> currentState.copy(isLoading = true) }

                viewModelScope.launch {
                    val result = authRepository.signInWithEmailAndPassword(_state.value.email, _state.value.password);

                    _state.update { currentState -> currentState.copy(isLoading = false) };

                    result.match(
                        onSuccess = { user ->
                            channel.send(Navigate(Screen.Home, user));
                        },
                        onFailure = { error ->
                            channel.send(ShowSnackBar(error.message));
                        }
                    )
                }
            }

           is SignInIntent.OnSignInWithGoogle -> {
                viewModelScope.launch {
                    val result = authRepository.signInWithGoogle(intent.context);

                    result.match(
                        onSuccess = { user ->
                            channel.send(Navigate(Screen.Home, user));
                        },
                        onFailure = { error ->
                            channel.send(ShowSnackBar(error.message));
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
                StringResource(R.string.blank_input_error,"email")
            ),
            Guard.against.pattern(
                _state.value.email,
                PatternConstants.EMAIL_PATTERN,
                "email",
                StringResource(R.string.invalid_email_error)
            )
        );

        val passwordValidationResult = Guard.against.validateAll(
            Guard.against.blankOrNull(
                _state.value.password,
                "password",
                StringResource(R.string.blank_input_error,"password")
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