package com.example.classscheduler.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable
import androidx.compose.ui.Modifier;
import com.example.classscheduler.ui.theme.ClassSchedulerTheme

@Serializable
data class HomeRoute(
    val email: String
);

@Composable
fun HomeScreen(
    email: String,
    modifier: Modifier = Modifier
):Unit{
    HomeScreenContent(
        email,
        modifier
    );
}

@Composable
private fun HomeScreenContent(
    email: String,
    modifier: Modifier = Modifier
):Unit{
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text("Hello $email to the home screen");
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        HomeScreenContent(
            email = "",
        );
    }
}
