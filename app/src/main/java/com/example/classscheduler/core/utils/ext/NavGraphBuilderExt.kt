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

/**
 * Defines all navigation destinations related to authentication flows such as
 * sign-in, sign-up, and password reset.
 *
 * Each screen exposes callbacks for navigation and for displaying snackbar
 * messages. Navigation is performed using extension helpers such as
 * [navigateSingleTop] and [navigateWithClearStack].
 *
 * @param nav Controller used to perform navigation actions.
 * @param scope Coroutine scope used to launch snackbar operations.
 * @param context Android context required for resolving string resources.
 * @param snackbar Host state used for showing snackbar messages.
 */
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
                nav.navigateSingleTop(SignInRoute);
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
                nav.navigateSingleTop(SignInRoute);
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

/**
 * Defines navigation destinations accessible from the home section of the app.
 *
 * Includes navigation into managing classes, daily schedules, and authentication
 * redirection when needed.
 *
 * @param nav Navigation controller used to handle screen transitions.
 * @param scope Coroutine scope for snackbar operations.
 * @param context Context used for resolving UI text resources.
 * @param snackbar Host state responsible for rendering snackbar messages.
 */
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

/**
 * Defines all navigation destinations for class management, including:
 * creation, updating, deleting, configuration, and viewing daily schedules.
 *
 * Each screen receives its required callbacks for local navigation and snackbar
 * messaging. All transitions use singleTop to avoid duplicating screens.
 *
 * @param nav Controller used for navigating between class-related screens.
 * @param scope Coroutine scope used when showing snackbar messages.
 * @param context Context used to resolve strings for snackbar messages.
 * @param snackbar Host state controlling snackbar rendering.
 */
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