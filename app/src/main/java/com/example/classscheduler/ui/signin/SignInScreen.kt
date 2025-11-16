package com.example.classscheduler.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.serialization.Serializable
import com.example.classscheduler.R;
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.ui.shared.AuthWithGoogleButton
import com.example.classscheduler.ui.shared.LoadingButton
import com.example.classscheduler.ui.shared.SingleLineTextField
import com.example.classscheduler.ui.theme.DarkBlue

@Serializable
object SignInRoute

@Composable
fun SignInScreen(
    openHomeScreen: () -> Unit,
    openSignUpScreen : () -> Unit,
    openResetPasswordScreen: () -> Unit,
    showSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = hiltViewModel(),
):Unit{
    val state by signInViewModel.state.collectAsStateWithLifecycle();

    val currentContext = LocalContext.current;

    signInViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate-> {
                when(event.destination){
                    Screen.Home -> openHomeScreen()
                    Screen.SignUp -> openSignUpScreen()
                    Screen.ResetPassword -> openResetPasswordScreen()
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar -> showSnackBar(event.message)
        }
    }

    SignInScreenContent(
        state,
        onEmailChange = { email -> signInViewModel.onIntent(SignInIntent.OnEmailChange(email))},
        onPasswordChange = { password -> signInViewModel.onIntent(SignInIntent.OnPasswordChange(password))},
        onPasswordVisibilityChange = { signInViewModel.onIntent(SignInIntent.OnPasswordVisibilityChange) },
        onSignUpButtonClicked = { signInViewModel.onIntent(SignInIntent.OnNavigateToSignUp)},
        onSignInButtonClicked = { signInViewModel.onIntent(SignInIntent.OnSignInWithEmailAndPassword) },
        onSignInWithGoogleClicked = { signInViewModel.onIntent(SignInIntent.OnSignInWithGoogle(currentContext))},
        onResetPasswordClicked = { signInViewModel.onIntent(SignInIntent.OnNavigateToResetPassword) },
        modifier
    )
}

@Composable
private fun SignInScreenContent(
    state: SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onSignUpButtonClicked: () -> Unit,
    onSignInButtonClicked: () -> Unit,
    onSignInWithGoogleClicked : () -> Unit,
    onResetPasswordClicked : () -> Unit,
    modifier: Modifier = Modifier
):Unit{
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = stringResource(R.string.sign_in_action),
            color = DarkBlue,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            letterSpacing = 0.1.em
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthWithGoogleButton(
            label = R.string.sign_in_with_google_action,
            onSignInWithGoogleClicked
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.Gray.copy(alpha = 0.5f)
            )

            Text(
                text = stringResource(R.string.or_text),
                color = Color.Gray.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 12.sp
            )

            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.Gray.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SingleLineTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = R.string.email_placeholder,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(R.string.email_placeholder)) },
            isError = state.emailError != null,
            supportingText = {
                state.emailError?.let { error ->
                    Text(
                        text = error.asString()
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp));

        SingleLineTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = R.string.password_placeholder,
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(R.string.password_placeholder)) },
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityChange) {
                    val visibilityIcon =
                        if (state.isPasswordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility;

                    val description = if(state.isPasswordHidden) stringResource(R.string.show_password) else stringResource(R.string.hide_password)
                    Icon(
                        imageVector = visibilityIcon,
                        contentDescription = description
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if(state.isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            isError = state.passwordError != null,
            supportingText = {
                state.passwordError?.let { error ->
                    Text(
                        text = error.asString()
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp));

        LoadingButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onClick = onSignInButtonClicked,
            isLoading = state.isLoading,
            enabled = true,
            label = R.string.sign_in_action
        )

        Spacer(modifier = Modifier.height(16.dp));

        TextButton(
            onClick = onResetPasswordClicked
        ) {
            Text(
                text = stringResource(R.string.forgot_password_question),
                color = DarkBlue,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 0.1.em
            )
        }

        Spacer(modifier = Modifier.height(16.dp));

        TextButton(
            onClick = onSignUpButtonClicked
        ) {
            Text(
                text = stringResource(R.string.no_account_question),
                textAlign = TextAlign.Center,
                color = DarkBlue,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 0.1.em
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        SignInScreenContent(
            state = SignInState(),
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onSignUpButtonClicked = {},
            onSignInButtonClicked = {},
            onSignInWithGoogleClicked = {},
            onResetPasswordClicked = {}
        );
    }
}