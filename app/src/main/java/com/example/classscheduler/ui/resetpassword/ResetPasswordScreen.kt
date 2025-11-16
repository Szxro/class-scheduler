package com.example.classscheduler.ui.resetpassword

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.classscheduler.ui.shared.LoadingButton
import com.example.classscheduler.ui.shared.SingleLineTextField
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                title = {},
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
    ){
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text =stringResource(R.string.reset_password_action),
                color = DarkBlue,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                letterSpacing = 0.1.em
            );

            Spacer(modifier = Modifier.height(8.dp));

            Text(
                text = stringResource(R.string.password_reset_message),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SingleLineTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                value = state.email,
                onValueChange = onEmailChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = R.string.email_placeholder,
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(R.string.email_placeholder)) },
                isError = state.emailHasError !== null,
                supportingText = {
                    state.emailHasError?.let { error ->
                        Text(
                            text = error.asString()
                        )
                    }
                });

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                label = R.string.reset_password_action,
                isLoading = state.isLoading,
                enabled = true,
                onClick = onResetPasswordButtonClicked,
            );
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
