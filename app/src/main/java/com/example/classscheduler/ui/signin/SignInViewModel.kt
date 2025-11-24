package com.example.classscheduler.ui.signin

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.ui.UiEvent.*
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.utils.validation.validators.SignInValidator
import com.example.classscheduler.domain.interfaces.AuthRepository
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel<SignInIntent, SignInState>() {
    private val _state = MutableStateFlow(SignInState());

    val state = _state.asStateFlow();

    init {
        addValidator(SignInValidator());
    }

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
                val validationResult = validator!!.validate(_state.value);

                _state.update { currentState ->
                    currentState.copy(
                        emailError = validationResult["email"]?.errorMessage,
                        passwordError = validationResult["password"]?.errorMessage
                    )
                }

                if(!validationResult.values.all { it.isValid }) return;

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

            SignInIntent.OnNavigateToResetPassword -> {
                viewModelScope.launch {
                    channel.send(Navigate(Screen.ResetPassword));
                }
            }
        }
    }
}