package com.example.classscheduler.core.utils.ext

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.classscheduler.ui.configureclass.ConfigureClassRoute
import com.example.classscheduler.ui.configureclass.ConfigureClassScreen
import com.example.classscheduler.ui.createclass.CreateClassRoute
import com.example.classscheduler.ui.createclass.CreateClassScreen
import com.example.classscheduler.ui.dayschedule.DayScheduleRoute
import com.example.classscheduler.ui.dayschedule.DayScheduleScreen
import com.example.classscheduler.ui.deleteclass.DeleteClassRoute
import com.example.classscheduler.ui.deleteclass.DeleteClassScreen
import com.example.classscheduler.ui.home.HomeRoute
import com.example.classscheduler.ui.home.HomeScreen
import com.example.classscheduler.ui.manageclasses.ManageClassesRoute
import com.example.classscheduler.ui.manageclasses.ManageClassesScreen
import com.example.classscheduler.ui.resetpassword.ResetPasswordRoute
import com.example.classscheduler.ui.resetpassword.ResetPasswordScreen
import com.example.classscheduler.ui.signin.SignInRoute
import com.example.classscheduler.ui.signin.SignInScreen
import com.example.classscheduler.ui.signup.SignUpRoute
import com.example.classscheduler.ui.signup.SignUpScreen
import com.example.classscheduler.ui.updateclass.UpdateClassRoute
import com.example.classscheduler.ui.updateclass.UpdateClassScreen
import kotlinx.coroutines.CoroutineScope

fun NavGraphBuilder.auth(
    nav: NavHostController,
    scope: CoroutineScope,
    context: Context,
    snackbar: SnackbarHostState,
): Unit{
    composable<SignInRoute> {
        SignInScreen(
            openHomeScreen = {
                nav.navigateWithClearStack(HomeRoute);
            },
            openSignUpScreen = {
                nav.navigateSingleTop(SignUpRoute)
            },
            openResetPasswordScreen = {
                nav.navigateSingleTop(ResetPasswordRoute)
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope
                )
            }
        )
    }
    composable<SignUpRoute> {
        SignUpScreen(
            openHomeScreen = {
                nav.navigateWithClearStack(HomeRoute);
            },
            openSignInScreen = {
                nav.navigateSingleTop(SignUpRoute);
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope
                )
            }
        );
    }
    composable<ResetPasswordRoute> {
        ResetPasswordScreen(
            openSignInScreen = {
                nav.navigateSingleTop(SignUpRoute);
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope,
                    SnackbarDuration.Long
                )
            }
        );
    }
}

fun NavGraphBuilder.home(
    nav: NavHostController,
    scope: CoroutineScope,
    context: Context,
    snackbar: SnackbarHostState,
){
    composable<HomeRoute> {
        HomeScreen(
            openSignInScreen = {
                nav.navigateWithClearStack(SignInRoute);
            },
            openManageClassesScreen = {
                nav.navigateSingleTop(ManageClassesRoute);
            },
            openDayScheduleScreen = { day ->
                nav.navigateSingleTop(DayScheduleRoute(day));
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope,
                )
            }
        );
    }
}

fun NavGraphBuilder.classes(
    nav: NavHostController,
    scope: CoroutineScope,
    context: Context,
    snackbar: SnackbarHostState,
): Unit{
    composable<CreateClassRoute>{
        CreateClassScreen(
            openManageClassesScreen = {
                nav.navigateSingleTop(ManageClassesRoute);
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope,
                )
            }
        )
    }

    composable<UpdateClassRoute>{
        UpdateClassScreen(
            openManageClass = {
                nav.navigateSingleTop(ManageClassesRoute);
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope,
                )
            }
        )
    }

    composable<DeleteClassRoute>{
        DeleteClassScreen(
            openManageClass = {
                nav.navigateSingleTop(ManageClassesRoute);
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope,
                )
            }
        )
    }

    composable<DayScheduleRoute>{
        DayScheduleScreen(
            openHomeScreen = {
                nav.navigateSingleTop(HomeRoute);
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope,
                )
            }
        )
    }

    composable<ConfigureClassRoute>{
        ConfigureClassScreen(
            onNavigateToManageClass = {
                nav.navigateSingleTop(ManageClassesRoute);
            },
            showSnackBar = { text ->
                snackbar.showMessage(
                    text,
                    context,
                    scope,
                )
            }
        )
    }

    composable<ManageClassesRoute>{
        ManageClassesScreen(
            openHomeScreen ={
                nav.navigateSingleTop(HomeRoute);
            },
            openCreateClassScreen = {
                nav.navigateSingleTop(CreateClassRoute);
            },
            openUpdateClassScreen = {
                nav.navigateSingleTop(UpdateClassRoute);
            },
            openDeleteClassScreen = {
                nav.navigateSingleTop(DeleteClassRoute);
            },
            openConfigureClassScreen = {
                nav.navigateSingleTop(ConfigureClassRoute);
            },
        );
    }
}