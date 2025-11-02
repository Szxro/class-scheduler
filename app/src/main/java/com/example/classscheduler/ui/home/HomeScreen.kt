package com.example.classscheduler.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable
import androidx.compose.ui.Modifier;
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.ui.theme.ClassSchedulerTheme

@Serializable
object HomeRoute;

@Composable
fun HomeScreen(
    openSignInScreen : () -> Unit,
    showSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
):Unit{
    val state by homeViewModel.state.collectAsStateWithLifecycle();

    homeViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate -> {
                when(event.destination){
                    Screen.SignIn -> openSignInScreen()
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar ->  showSnackBar(event.message)
        }
    }

    HomeScreenContent(
        state,
        onLogoutClicked = { homeViewModel.onIntent(HomeIntent.OnLogoOut) },
        modifier
    );
}

@Composable
private fun HomeScreenContent(
    state: HomeState,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier
):Unit{
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator();
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Hello ${state.currentUser?.email ?: "NOT EMAIL FOUND"} to the home screen")
                Button(
                    onClick = onLogoutClicked
                ) {
                    Text("Log Out");
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        HomeScreenContent(
            state = HomeState(),
            onLogoutClicked = {},
        );
    }
}
