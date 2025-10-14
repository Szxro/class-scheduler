package com.example.classscheduler.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.serialization.Serializable

@Serializable
object SignUpRoute

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier
):Unit{
    SignUpScreenContent(modifier);
}

@Composable
private fun SignUpScreenContent(
    modifier: Modifier = Modifier
):Unit{
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ){
        Text("Sign Up Screen");
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        SignUpScreenContent();
    }
}
