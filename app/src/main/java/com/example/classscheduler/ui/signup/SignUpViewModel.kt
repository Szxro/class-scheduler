package com.example.classscheduler.ui.signup

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.classscheduler.R;
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiEvent.ShowSnackBar
import com.example.classscheduler.core.ui.UiText.StringResource
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.validation.validators.SignUpValidator
import com.example.classscheduler.domain.interfaces.AuthRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: AuthRepository
) : BaseViewModel<SignUpIntent, SignUpState>() {
    private val _state = MutableStateFlow(SignUpState());

    val state = _state.asStateFlow();

    init {
        addValidator(SignUpValidator());
    }

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
                val validationResult = this.validator!!.validate(_state.value);

                _state.update { currentState -> currentState.copy(
                    emailHasError = validationResult["email"]?.errorMessage,
                    passwordHasError = validationResult["password"]?.errorMessage,
                    confirmPasswordHasError = validationResult["confirm_password"]?.errorMessage
                ) }

                if(!validationResult.values.all { it.isValid }) return;

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
}