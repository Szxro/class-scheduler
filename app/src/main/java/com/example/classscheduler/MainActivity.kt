package com.example.classscheduler

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.classscheduler.core.utils.ext.auth
import com.example.classscheduler.core.utils.ext.classes
import com.example.classscheduler.core.utils.ext.home
import com.example.classscheduler.data.datasource.AuthRemoteDataSource
import com.example.classscheduler.ui.home.HomeRoute
import com.example.classscheduler.ui.signin.SignInRoute
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authRemoteDataSource: AuthRemoteDataSource


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                    ) {
                        // Auth Graph
                        auth(
                            navController,
                            scope,
                            this@MainActivity,
                            snackBarHostState
                        )

                        // Home Graph
                        home(
                            navController,
                            scope,
                            this@MainActivity,
                            snackBarHostState
                        )

                        // Classes graph (create/update/delete/configure)
                        classes(
                            navController,
                            scope,
                            this@MainActivity,
                            snackBarHostState
                        )
                    }
                }
            }
        }
    }
}