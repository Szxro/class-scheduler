package com.example.classscheduler.ui.dayschedule

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.classscheduler.R
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.core.utils.ext.toLocalTime
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.domain.models.Schedule
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter

@Serializable
data class DayScheduleRoute(val day: String);

@Composable
fun  DayScheduleScreen(
    openHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    dayScheduleViewModel: DayScheduleViewModel = hiltViewModel(),
    showSnackBar: (UiText) -> Unit
): Unit{
    val state by dayScheduleViewModel.state.collectAsStateWithLifecycle();

    dayScheduleViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate -> {
                when(event.destination){
                    Screen.Home -> openHomeScreen()
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar -> showSnackBar(event.message)
        }
    }

    DayScheduleScreenContent(
        state = state,
        onNavigateToHomeScreen = {dayScheduleViewModel.onIntent(DayScheduleIntent.OnNavigateToHomeScreen)},
        modifier
    );
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DayScheduleScreenContent(
    state: DayScheduleState,
    onNavigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
): Unit{
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("${state.day} Classes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHomeScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back_action)
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ){
                when{
                    state.isLoading -> LoadingIndicator(state.day)
                    state.classes.isEmpty() -> EmptyDayClassSection(state.day)
                    else -> ClassesListSection(classes = state.classes)
                }
            }
        }
    }
}

// TODD: REFACTOR COMPONENTS TO MAKE IT MORE REUSABLE
@Composable
private fun LoadingIndicator(day: String): Unit {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Loading $day's classes...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EmptyDayClassSection(day: String): Unit {
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
            text = "No classes scheduled for $day",
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

@Preview(showSystemUi = true)
@Composable
private fun DayScheduleScreenPreview(): Unit{
    ClassSchedulerTheme(darkTheme = true){
        DayScheduleScreenContent(
            state = DayScheduleState(),
            onNavigateToHomeScreen = {},
        );
    }
}