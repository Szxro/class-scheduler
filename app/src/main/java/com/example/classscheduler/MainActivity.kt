package com.example.classscheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.classscheduler.ui.home.HomeRoute
import com.example.classscheduler.ui.home.HomeScreen
import com.example.classscheduler.ui.signin.SignInRoute
import com.example.classscheduler.ui.signin.SignInScreen
import com.example.classscheduler.ui.signup.SignUpRoute
import com.example.classscheduler.ui.signup.SignUpScreen
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController();
            ClassSchedulerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController, startDestination = SignInRoute, modifier = Modifier.padding(innerPadding)){
                        composable<SignInRoute>{
                            SignInScreen(
                                openHomeScreen = {
                                    navController.navigate(HomeRoute){
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
                                }
                            )
                        }
                        composable<SignUpRoute>{
                            SignUpScreen(

                            );
                        }
                        composable<HomeRoute>{
                            HomeScreen(

                            );
                        }
                    }
                }
            }
        }
    }
}