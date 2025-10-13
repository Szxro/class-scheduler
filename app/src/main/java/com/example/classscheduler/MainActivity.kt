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
import com.example.classscheduler.ui.signin.SignInRoute
import com.example.classscheduler.ui.signin.SignInScreen
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
                                openHomeScreen = {},
                                openSignUpScreen = {}
                            )
                        }
                    }
                }
            }
        }
    }
}