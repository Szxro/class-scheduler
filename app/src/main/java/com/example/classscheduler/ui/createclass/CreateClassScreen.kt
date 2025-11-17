package com.example.classscheduler.ui.createclass

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.ui.shared.LoadingButton
import com.example.classscheduler.ui.shared.SelectableDropdownMenu
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.serialization.Serializable
import com.example.classscheduler.R;
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.ui.shared.ScheduleRow
import com.example.classscheduler.ui.shared.SingleLineTextField
import com.example.classscheduler.ui.shared.SingleSelectDropDown


@Serializable
object CreateClassRoute

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CreateClassScreen(
    openManageClassesScreen: () -> Unit,
    showSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    createClassViewModel: CreateClassViewModel = hiltViewModel(),
): Unit {

    val state by createClassViewModel.state.collectAsStateWithLifecycle();

    createClassViewModel.events.ObserveEventsAs { event ->
        when (event) {
            is UiEvent.Navigate -> {
                when (event.destination) {
                    Screen.ManageClasses -> openManageClassesScreen();
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar -> showSnackBar(event.message)
        }
    }

    CreateClassScreenContent(
        state = state,
        onNameChange = { name -> createClassViewModel.onIntent(CreateClassIntent.OnNameChange(name)) },
        onCodeChange = { code -> createClassViewModel.onIntent(CreateClassIntent.OnCodeChange(code)) },
        onTeacherChange = { teacher -> createClassViewModel.onIntent(CreateClassIntent.OnTeacherChange(teacher)) },
        onClassRoomChange = { classRoom -> createClassViewModel.onIntent(CreateClassIntent.OnClassRoomChange(classRoom)) },
        onNavigateToManageClasses = { createClassViewModel.onIntent(CreateClassIntent.OnNavigateToManageClasses) },
        onDaysChange = { days ->  createClassViewModel.onIntent(CreateClassIntent.OnDaysChange(days))},
        onScheduleChange = {day, start, end -> createClassViewModel.onIntent(CreateClassIntent.OnScheduleChange(day,start,end))},
        onSaveClicked = { createClassViewModel.onIntent(CreateClassIntent.OnSaveClicked) },
        modifier = modifier
    );
}


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateClassScreenContent(
    state: CreateClassState,
    onNavigateToManageClasses: () -> Unit,
    onNameChange : (String) -> Unit,
    onCodeChange : (String) -> Unit,
    onTeacherChange : (String) -> Unit,
    onClassRoomChange : (String) -> Unit,
    onDaysChange : (List<String>) -> Unit,
    onScheduleChange: (String,Long?, Long?) -> Unit,
    onSaveClicked : () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.create_class_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToManageClasses) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back_action)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                SingleLineTextField(
                    value = state.name,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = onNameChange,
                    placeholder = R.string.name_placeholder,
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    isError = state.nameHasError != null,
                    supportingText = {
                        state.nameHasError?.let { text ->
                            Text(text = text.asString())
                        }
                    }
                )
                
                SingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.code,
                    onValueChange = onCodeChange,
                    leadingIcon = { Icon(Icons.Default.Code, contentDescription = null) },
                    placeholder = R.string.code_placeholder,
                    isError = state.codeHasError != null,
                    supportingText = {
                        state.codeHasError?.let { text ->
                            Text(text = text.asString())
                        }
                    }
                )

                SingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = R.string.teacher_placeholder,
                    value = state.teacher,
                    onValueChange = onTeacherChange,
                    isError = state.teacherHasError != null,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    supportingText = {
                        state.teacherHasError?.let { text ->
                            Text(text = text.asString())
                        }
                    }
                )

                SingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = R.string.classroom_placeholder,
                    value = state.classroom,
                    onValueChange = onClassRoomChange,
                    isError = state.classroomHasError != null,
                    leadingIcon = { Icon(Icons.Default.MeetingRoom, contentDescription = null) },
                    supportingText = {
                        state.classroomHasError?.let { text ->
                            Text(text = text.asString())
                        }
                    }
                )

                SelectableDropdownMenu(
                    options = listOf("Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"),
                    label = "Select day(s)",
                    itemLabel = { day -> day },
                    selected = state.schedule.map { it.day },
                    onSelectionChange = { selection -> onDaysChange(selection)}
                );

                state.schedule.forEach{ schedule ->
                    ScheduleRow(
                        schedule,
                        onTimeChange = { start, end -> onScheduleChange(schedule.day, start, end) }
                    )
                }

                state.scheduleHasError?.let { text ->
                    Text(
                        text = text.asString(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(12.dp));

                LoadingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = true,
                    isLoading = state.isLoading,
                    label = R.string.save_action,
                    onClick = onSaveClicked,
                    icon = Icons.Default.CheckCircle,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showSystemUi = true)
@Composable
private fun CreateClassScreenPreview(): Unit {
    ClassSchedulerTheme(darkTheme = true) {
        CreateClassScreenContent(
            state = CreateClassState(),
            onNameChange = {},
            onCodeChange = {},
            onTeacherChange = {},
            onClassRoomChange = {},
            onNavigateToManageClasses = {},
            onScheduleChange = {day, start, end -> },
            onSaveClicked = {},
            onDaysChange = {}
        )
    }
}