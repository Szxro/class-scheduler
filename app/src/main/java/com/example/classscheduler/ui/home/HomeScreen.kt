package com.example.classscheduler.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable
import androidx.compose.ui.Modifier;
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.coroutines.launch
import com.example.classscheduler.R;
import com.example.classscheduler.core.utils.constants.DateConstants
import com.example.classscheduler.core.utils.ext.toLocalTime
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.domain.models.Schedule
import java.time.format.DateTimeFormatter

@Serializable
object HomeRoute;

@Composable
fun HomeScreen(
    openSignInScreen: () -> Unit,
    openManageClassesScreen: () -> Unit,
    openDayScheduleScreen: (String) -> Unit,
    showSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
): Unit {
    val state by homeViewModel.state.collectAsStateWithLifecycle();

    homeViewModel.events.ObserveEventsAs { event ->
        when (event) {
            is UiEvent.Navigate -> {
                when (event.destination) {
                    Screen.SignIn -> openSignInScreen()
                    Screen.ManageClasses -> openManageClassesScreen()
                    Screen.DaySchedule -> openDayScheduleScreen(event.args as String);
                    else -> Unit
                }
            }

            is UiEvent.ShowSnackBar -> showSnackBar(event.message)
        }
    }
    HomeScreenContent(
        state,
        onLogoutClicked = { homeViewModel.onIntent(HomeIntent.OnLogoOut) },
        onManageClassesClicked = { homeViewModel.onIntent(HomeIntent.OnManageClassesClicked) },
        onNavigateToDayScheduleScreen = {day -> homeViewModel.onIntent(HomeIntent.OnNavigateToDaySchedule(day))},
        modifier
    );
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    state: HomeState,
    onLogoutClicked: () -> Unit,
    onManageClassesClicked: () -> Unit,
    onNavigateToDayScheduleScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope();

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            state.currentUser?.photoUrl?.let {
                                AsyncImage(
                                    model = it,
                                    contentDescription = stringResource(R.string.profile_picture),
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = 2.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = CircleShape
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            } ?: Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = stringResource(R.string.default_profile_picture),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(90.dp)
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = state.currentUser?.email
                                    ?: stringResource(R.string.unknown_email),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp));

                    HorizontalDivider();

                    NavigationDrawerItem(
                        label = { Text("Manage Classes") },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Manage Classes"
                            )
                        },
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    close();
                                    onManageClassesClicked();
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    HorizontalDivider();

                    Spacer(Modifier.height(12.dp));

                    Text(
                        text = stringResource(R.string.classes_by_day),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )

                    DateConstants.DAYS_OF_THE_WEEK.forEach { day ->
                        NavigationDrawerItem(
                            label = { Text(day) },
                            selected = false,
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Classes on $day"
                                )
                            },
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        close()
                                        onNavigateToDayScheduleScreen(day);
                                    }
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.logout_action)) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout Button"
                            )
                        },
                        onClick = {
                            onLogoutClicked()
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Home") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close();
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                val displayName = when {
                    !state.currentUser?.displayName.isNullOrBlank() ->
                        state.currentUser.displayName

                    !state.currentUser?.email.isNullOrBlank() ->
                        state.currentUser.email?.substringBefore("@")
                    else -> "User"
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Welcome back, $displayName",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    );

                    HorizontalDivider();

                    Text(
                        text = "Today's classes",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    );

                    when {
                        // Show loading indicator
                        state.isLoading -> LoadingIndicator()
                        // Empty State
                        state.currentClasses.isEmpty() -> EmptyClassSection()
                        //Show today classes
                        else -> ClassesListSection(state.currentClasses)
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator(): Unit {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Loading today’s classes...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EmptyClassSection(): Unit {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No classes scheduled for today",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ClassesListSection(classes: List<Class>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(classes) { classItem ->
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = classItem.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Teacher: ${classItem.teacher}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Classroom: ${classItem.classroom}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Schedule",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    classItem.schedule.forEach { schedule ->
                        ScheduleItem(schedule)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleItem(schedule: Schedule) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(12.dp)
    ) {

        AssistChip(
            onClick = {},
            label = {
                Text(
                    schedule.day,
                    fontWeight = FontWeight.Bold
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null
                )
            }
        )

        Spacer(Modifier.height(8.dp))

        ScheduleTimeRow(
            icon = Icons.Default.AccessTime,
            label = "Start",
            time = schedule.startTimeLong?.toLocalTime()
                ?.format(DateTimeFormatter.ofPattern("hh:mm a"))
                ?: "Not registered"
        )

        Spacer(Modifier.height(4.dp))

        ScheduleTimeRow(
            icon = Icons.Default.Schedule,
            label = "End",
            time = schedule.endTimeLong?.toLocalTime()
                ?.format(DateTimeFormatter.ofPattern("hh:mm a"))
                ?: "Not registered"
        )
    }
}

@Composable
fun ScheduleTimeRow(
    icon: ImageVector,
    label: String,
    time: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "$label: $time",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview(): Unit {
    ClassSchedulerTheme(darkTheme = true) {
        HomeScreenContent(
            state = HomeState(),
            onLogoutClicked = {},
            onManageClassesClicked = {},
            onNavigateToDayScheduleScreen = {}
        );
    }
}
