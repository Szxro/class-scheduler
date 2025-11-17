package com.example.classscheduler.ui.updateclass

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.ui.shared.ScheduleRow
import com.example.classscheduler.ui.shared.SelectableDropdownMenu
import com.example.classscheduler.ui.shared.SingleLineTextField
import com.example.classscheduler.ui.shared.SingleSelectDropDown
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.serialization.Serializable

@Serializable
object UpdateClassRoute

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun UpdateClassScreen(
    openManageClass: () -> Unit,
    modifier: Modifier = Modifier,
    showSnackBar: (UiText) -> Unit,
    updateClassViewModel: UpdateClassViewModel = hiltViewModel(),
):Unit{
    val state by updateClassViewModel.state.collectAsStateWithLifecycle();

    updateClassViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate -> {
                when(event.destination){
                    Screen.ManageClasses -> openManageClass()
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar -> showSnackBar(event.message)
        }
    }

    UpdateClassScreenContent(
        state = state,
        modifier = modifier,
        onNavigateToManageClass = { updateClassViewModel.onIntent(UpdateClassIntent.OnNavigateToManageClasses) },
        onClassSelected = { selectedClass -> updateClassViewModel.onIntent(UpdateClassIntent.OnSelectedClass(selectedClass)) },
        onUpdateClass = { updateClassViewModel.onIntent(UpdateClassIntent.OnUpdateClass) },
        onNameChange = { name -> updateClassViewModel.onIntent(UpdateClassIntent.OnNameChange(name)) },
        onClassRoomChange = { classRoom -> updateClassViewModel.onIntent(UpdateClassIntent.OnClassRoomChange(classRoom)) },
        onCodeChange = { code -> updateClassViewModel.onIntent(UpdateClassIntent.OnCodeChange(code)) },
        onDaysChange = { days -> updateClassViewModel.onIntent(UpdateClassIntent.OnDaysChange(days)) },
        onTeacherChange = { teacher -> updateClassViewModel.onIntent(UpdateClassIntent.OnTeacherChange(teacher)) },
        onScheduleChange = { day, start, end -> updateClassViewModel.onIntent(UpdateClassIntent.OnScheduleChange(day, start,end)) }
    );
}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateClassScreenContent(
    state: UpdateClassState,
    onNavigateToManageClass : () -> Unit,
    onClassSelected: (Class?) -> Unit,
    onUpdateClass: () -> Unit,
    onNameChange : (String) -> Unit,
    onCodeChange : (String) -> Unit,
    onTeacherChange : (String) -> Unit,
    onClassRoomChange : (String) -> Unit,
    onDaysChange : (List<String>) -> Unit,
    onScheduleChange: (String,Long?, Long?) -> Unit,
    modifier: Modifier = Modifier
):Unit{
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.update_class_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToManageClass) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back_action)
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
            ){
                when{
                    state.isLoading -> LoadingIndicator()
                    state.classes.isEmpty() -> EmptyClasses()
                    else -> UpdateClassContentSection(
                        state,
                        onClassSelected = onClassSelected,
                        onUpdateClass = onUpdateClass,
                        onNameChange = onNameChange,
                        onCodeChange = onCodeChange,
                        onTeacherChange = onTeacherChange,
                        onClassRoomChange = onClassRoomChange,
                        onDaysChange = onDaysChange,
                        onScheduleChange = onScheduleChange
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun UpdateClassContentSection(
    state: UpdateClassState,
    onClassSelected: (Class?) -> Unit,
    onUpdateClass: () -> Unit,
    onNameChange : (String) -> Unit,
    onCodeChange : (String) -> Unit,
    onTeacherChange : (String) -> Unit,
    onClassRoomChange : (String) -> Unit,
    onDaysChange : (List<String>) -> Unit,
    onScheduleChange: (String,Long?, Long?) -> Unit,
):Unit{
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ){
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Text(
                    text = "Select the class to update",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                SingleSelectDropDown(
                    label = "Select class",
                    options = state.classes,
                    itemLabel = { classes -> classes.name },
                    onSelection = { onClassSelected(it) }
                )

                state.selectedClassHasError?.let { text ->
                    Text(
                        text = text.asString(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                state.selectedClass?.let { classItem ->
                    SingleLineTextField(
                        value = classItem.name,
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
                        value = classItem.code,
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
                        value = classItem.teacher,
                        onValueChange = onTeacherChange,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        isError = state.teacherHasError != null,
                        supportingText = {
                            state.teacherHasError?.let { text ->
                                Text(text = text.asString())
                            }
                        }
                    )

                    SingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = R.string.classroom_placeholder,
                        value = classItem.classroom,
                        onValueChange = onClassRoomChange,
                        leadingIcon = { Icon(Icons.Default.MeetingRoom, contentDescription = null) },
                        isError = state.classroomHasError != null,
                        supportingText = {
                            state.classroomHasError?.let { text ->
                                Text(text = text.asString())
                            }
                        }
                    )

                    SelectableDropdownMenu(
                        options = listOf("Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"),
                        label = "Select day(s)",
                        selected = state.selectedClass.schedule.map { it.day },
                        itemLabel = { day -> day },
                        onSelectionChange = { selection -> onDaysChange(selection) }
                    );

                    classItem.schedule.forEach{ schedule ->
                        ScheduleRow(
                            schedule,
                            onTimeChange = { start, end ->  onScheduleChange(schedule.day,start,end)}
                        )
                    }

                    state.scheduleHasError?.let { text ->
                        Text(
                            text = text.asString(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp),
            onClick = onUpdateClass,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.update_class_title))
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
            text = "Loading classes...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EmptyClasses(): Unit {
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
            text = "No classes are register",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(showSystemUi = true)
@Composable
private fun UpdateClassScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        UpdateClassScreenContent(
            state = UpdateClassState(),
            onNavigateToManageClass = {},
            onClassSelected = {},
            onUpdateClass = {},
            onNameChange = {},
            onClassRoomChange = {},
            onDaysChange = {},
            onCodeChange = {},
            onScheduleChange = { day ,start, end -> },
            onTeacherChange = {}
        );
    }
}