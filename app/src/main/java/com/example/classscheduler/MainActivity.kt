package com.example.classscheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.classscheduler.core.utils.ext.showMessage
import com.example.classscheduler.data.datasource.AuthRemoteDataSource
import com.example.classscheduler.ui.home.HomeRoute
import com.example.classscheduler.ui.home.HomeScreen
import com.example.classscheduler.ui.resetpassword.ResetPasswordRoute
import com.example.classscheduler.ui.resetpassword.ResetPasswordScreen
import com.example.classscheduler.ui.signin.SignInRoute
import com.example.classscheduler.ui.signin.SignInScreen
import com.example.classscheduler.ui.signup.SignUpRoute
import com.example.classscheduler.ui.signup.SignUpScreen
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authRemoteDataSource: AuthRemoteDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen();
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController();
            val scope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }
            // if the current user is active the startDestination is going to be Home
            val startDestination =
                authRemoteDataSource.currentUser?.let { HomeRoute } ?: SignInRoute;

            ClassSchedulerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<SignInRoute> {
                            SignInScreen(
                                openHomeScreen = {
                                    navController.navigate(HomeRoute) {
                                        // when the user navigate to home is going  to delete the hold stack [SignIn, SignUp,ResetPassword] -> [Home]
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                        }
                                        launchSingleTop =
                                            true; // Avoid multiple instances of the same screen
                                    };
                                },
                                openSignUpScreen = {
                                    navController.navigate(SignUpRoute) {
                                        launchSingleTop = true;
                                    };
                                },
                                openResetPasswordScreen = {
                                    navController.navigate(ResetPasswordRoute) {
                                        launchSingleTop = true;
                                    }
                                },
                                showSnackBar = { text ->
                                    snackBarHostState.showMessage(
                                        text,
                                        this@MainActivity,
                                        scope
                                    )
                                }
                            )
                        }
                        composable<SignUpRoute> {
                            SignUpScreen(
                                openHomeScreen = {
                                    navController.navigate(HomeRoute) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true;
                                    };
                                },
                                openSignInScreen = {
                                    navController.navigate(SignInRoute) {
                                        launchSingleTop = true;
                                    }
                                },
                                showSnackBar = { text ->
                                    snackBarHostState.showMessage(
                                        text,
                                        this@MainActivity,
                                        scope
                                    )
                                }
                            );
                        }
                        composable<ResetPasswordRoute> {
                            ResetPasswordScreen(
                                openSignInScreen = {
                                    navController.navigate(SignInRoute) {
                                        launchSingleTop = true;
                                    }
                                },
                                showSnackBar = { text ->
                                    snackBarHostState.showMessage(
                                        text,
                                        this@MainActivity,
                                        scope,
                                        SnackbarDuration.Long
                                    )
                                }
                            );
                        }
                        composable<HomeRoute> {
                            HomeScreen(
                                openSignInScreen = {
                                    navController.navigate(SignInRoute) {
                                        // Resetting the nav graph
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true;
                                    }
                                },
                                showSnackBar = { text ->
                                    snackBarHostState.showMessage(
                                        text,
                                        this@MainActivity,
                                        scope,
                                    )
                                }
                            );
                        }
                    }
                }
            }
        }
    }
}