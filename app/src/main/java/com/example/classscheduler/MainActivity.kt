package com.example.classscheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.classscheduler.ui.home.HomeRoute
import com.example.classscheduler.ui.home.HomeScreen
import com.example.classscheduler.ui.signin.SignInRoute
import com.example.classscheduler.ui.signin.SignInScreen
import com.example.classscheduler.ui.signup.SignUpRoute
import com.example.classscheduler.ui.signup.SignUpScreen
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController();
            val scope = rememberCoroutineScope();
            val snackBarHostState = remember { SnackbarHostState() }

            ClassSchedulerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState)
                    }) { innerPadding ->
                    NavHost(navController, startDestination = SignInRoute, modifier = Modifier.padding(innerPadding)){
                        composable<SignInRoute>{
                            SignInScreen(
                                openHomeScreen = { user ->
                                    navController.navigate(HomeRoute(user.email)){
                                        popUpTo(SignInRoute){
                                            inclusive = true; // Remove the SignInRoute from the stack
                                        }
                                        launchSingleTop = true; // Avoid multiple instances of the same screen
                                    };
                                },
                                openSignUpScreen = {
                                    navController.navigate(SignUpRoute){
                                        launchSingleTop = true;
                                    };
                                },
                                showErrorSnackBar = {error ->
                                    val message = error.asString(this@MainActivity);

                                    scope.launch { snackBarHostState.showSnackbar(message) };
                                }
                            )
                        }
                        composable<SignUpRoute>{
                            SignUpScreen(

                            );
                        }
                        composable<HomeRoute>{ entry ->
                            val (email) = entry.toRoute<HomeRoute>();

                            HomeScreen(
                                email
                            );
                        }
                    }
                }
            }
        }
    }
}