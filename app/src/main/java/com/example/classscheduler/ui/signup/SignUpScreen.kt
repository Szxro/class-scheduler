package com.example.classscheduler.ui.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.classscheduler.R
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.domain.models.User
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import com.example.classscheduler.ui.theme.DarkBlue
import kotlinx.serialization.Serializable

@Serializable
object SignUpRoute

@Composable
fun SignUpScreen(
    openHomeScreen: (user: User) -> Unit,
    openSignInScreen: () -> Unit,
    showErrorSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
):Unit{
    val state by signUpViewModel.state.collectAsStateWithLifecycle();

    val currentContext = LocalContext.current;

    signUpViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate -> {
                when(event.destination){
                    is Screen.Home -> openHomeScreen(event.args!! as User)
                    is Screen.SignIn -> openSignInScreen()
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar -> showErrorSnackBar(event.message)
        }
    }

    SignUpScreenContent(
        state,
        onEmailChange = { email -> signUpViewModel.onIntent(SignUpIntent.OnEmailChange(email)) },
        onPasswordChange = { password -> signUpViewModel.onIntent(SignUpIntent.OnPasswordChange(password)) },
        onConfirmPasswordChange = { confirmPassword -> signUpViewModel.onIntent(SignUpIntent.OnConfirmPasswordChange(confirmPassword)) },
        onPasswordVisibilityChange = { signUpViewModel.onIntent(SignUpIntent.OnPasswordVisibilityChange) },
        onSignInWithGoogleClicked = {signUpViewModel.onIntent(SignUpIntent.OnSignUpWithGoogle(currentContext)) },
        onCreateButtonClicked = { signUpViewModel.onIntent(SignUpIntent.OnSignUp) },
        onNavigateToSignIn = {signUpViewModel.onIntent(SignUpIntent.OnNavigateToSignIn)},
        modifier = modifier
    );
}

@Composable
private fun SignUpScreenContent(
    state: SignUpState,
    onEmailChange: (String) -> Unit,
    onPasswordChange : (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange : () -> Unit,
    onCreateButtonClicked : () -> Unit,
    onSignInWithGoogleClicked : () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
):Unit{
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = stringResource(R.string.sign_up_action),
            textAlign = TextAlign.Center,
            color = DarkBlue,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            letterSpacing = 0.1.em
        )
        Spacer(modifier = Modifier.height(24.dp));

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onClick = onSignInWithGoogleClicked ,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = DarkBlue,
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, DarkBlue)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(R.drawable.google_logo),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )

                Text(
                    text = stringResource(R.string.sign_with_google_action),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    letterSpacing = 0.1.em
                )
            }
        }


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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            value = state.email,
            onValueChange = onEmailChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            placeholder = { Text(text = stringResource(R.string.email_placeholder)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email_placeholder)
                )
            },
            isError = state.emailHasError != null,
            supportingText = {
                state.emailHasError?.let { error ->
                    Text(
                        text = error.asString()
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp));

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            singleLine = true,
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = { Text(text = stringResource(R.string.password_placeholder)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(R.string.password_placeholder)
                )
            },
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityChange) {
                    val visibilityIcon =
                        if (state.isPasswordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility;

                    val description =
                        if (state.isPasswordHidden) stringResource(R.string.show_password) else stringResource(
                            R.string.hide_password
                        )
                    Icon(
                        imageVector = visibilityIcon,
                        contentDescription = description
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (state.isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            isError = state.passwordHasError != null,
            supportingText = {
                state.passwordHasError?.let { error ->
                    Text(
                        text = error.asString()
                    )
                }
            });

        Spacer(modifier = Modifier.height(8.dp));

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            singleLine = true,
            value = state.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = { Text(text = "Confirm password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(R.string.password_placeholder)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (state.isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            isError = state.confirmPasswordHasError != null,
            supportingText = {
                state.confirmPasswordHasError?.let { error ->
                    Text(
                        text = error.asString()
                    )
                }
            });

        Spacer(modifier = Modifier.height(8.dp));

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onClick = onCreateButtonClicked,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = DarkBlue,
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, DarkBlue)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.continue_text),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    letterSpacing = 0.1.em
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp));

        TextButton(
            onClick = onNavigateToSignIn
        ) {
            Text(
                text = stringResource(R.string.have_an_account_question),
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
private fun SignUpScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        SignUpScreenContent(
            state = SignUpState(),
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onSignInWithGoogleClicked = {},
            onCreateButtonClicked = {},
            onConfirmPasswordChange = {},
            onNavigateToSignIn = {}
        );
    }
}
