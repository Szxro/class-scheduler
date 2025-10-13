package com.example.classscheduler.ui.signin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.serialization.Serializable
import com.example.classscheduler.R;
import com.example.classscheduler.ui.theme.DarkBlue

@Serializable
object SignInRoute

@Composable
fun SignInScreen(
    openHomeScreen: () -> Unit,
    openSignUpScreen : () -> Unit,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = hiltViewModel(),
):Unit{
    var email by rememberSaveable { mutableStateOf("") };

    var password by rememberSaveable { mutableStateOf("") };

    SignInScreenContent(
        email,
        password,
        onEmailChange = { email = it},
        onPasswordChange = { password = it },
        openHomeScreen,
        openSignUpScreen,
        modifier,
    )
}

@Composable
private fun SignInScreenContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    openHomeScreen: () -> Unit,
    openSignUpScreen: () -> Unit,
    modifier: Modifier = Modifier
):Unit{
    var isPasswordHidden by rememberSaveable { mutableStateOf(true) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
           painter = painterResource(R.drawable.ic_scheduler_logo),
            contentDescription = null
        );
        Spacer(modifier = Modifier.height(16.dp));

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            singleLine = true,
            value = email,
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            placeholder = { Text(text = stringResource(R.string.email_input_placeholder))},
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(R.string.email_input_placeholder)) }
        )

        Spacer(modifier = Modifier.height(16.dp));

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            singleLine = true,
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text(text = stringResource(R.string.password_input_placeholder))},
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(R.string.password_input_placeholder)) },
            trailingIcon = {
                IconButton(onClick = { isPasswordHidden = !isPasswordHidden}) {
                    val visibilityIcon =
                        if (isPasswordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility;

                    val description = if(isPasswordHidden) "Show password" else "Hide password";
                    Icon(
                        imageVector = visibilityIcon,
                        contentDescription = description
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if(isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(16.dp));

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onClick = openHomeScreen,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = DarkBlue,
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, DarkBlue)
        ) {
            Text(
                text = stringResource(R.string.sign_in_with_email),
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp));

        TextButton(
            onClick = openSignUpScreen
        ) {
            Text(
                text = stringResource(R.string.sign_up_text),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = DarkBlue
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        SignInScreenContent(
            email = "",
            password = "",
            onEmailChange = {},
            onPasswordChange = {},
            openHomeScreen = {},
            openSignUpScreen = {}
        );
    }
}