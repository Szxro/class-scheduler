package com.example.classscheduler.ui.resetpassword

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.serialization.Serializable
import com.example.classscheduler.R;
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.ui.theme.DarkBlue

@Serializable
object ResetPasswordRoute

@Composable
fun ResetPasswordScreen(
    openSignInScreen: () -> Unit,
    showSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel(),
):Unit{
    val state by resetPasswordViewModel.state.collectAsStateWithLifecycle();

    resetPasswordViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate -> {
                when(event.destination){
                    Screen.SignIn -> openSignInScreen()
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar -> showSnackBar(event.message);
        }
    }

    ResetPasswordScreenContent(
        state,
        onEmailChange = {email -> resetPasswordViewModel.onIntent(ResetPasswordIntent.OnEmailChange(email))},
        onResetPasswordButtonClicked = { resetPasswordViewModel.onIntent(ResetPasswordIntent.OnResetPasswordButtonClicked)},
        onNavigateToSignIn = {resetPasswordViewModel.onIntent(ResetPasswordIntent.OnNavigateToSignIn)},
        modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResetPasswordScreenContent(
    state: ResetPasswordState,
    onEmailChange: (String) -> Unit,
    onResetPasswordButtonClicked: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
):Unit{
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text =stringResource(R.string.reset_password_action),
                        color = DarkBlue,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        letterSpacing = 0.1.em
                    );

                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateToSignIn
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){ paddingValues ->

        Column(
            modifier = modifier.fillMaxSize()
                               .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = stringResource(R.string.password_reset_message),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                value = state.email,
                onValueChange = onEmailChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = { Text(text = stringResource(R.string.email_placeholder))},
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(R.string.email_placeholder)) },
                isError = state.emailHasError !== null,
                supportingText = {
                    state.emailHasError?.let { error ->
                        Text(
                            text = error.asString()
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                onClick = onResetPasswordButtonClicked,
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
                        text = stringResource(R.string.reset_password_action),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        letterSpacing = 0.1.em
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ResetPasswordScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        ResetPasswordScreenContent(
            state = ResetPasswordState(),
            onEmailChange = {},
            onNavigateToSignIn = {},
            onResetPasswordButtonClicked = {}
        );
    }
}
