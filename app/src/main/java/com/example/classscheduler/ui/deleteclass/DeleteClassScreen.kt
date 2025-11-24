package com.example.classscheduler.ui.deleteclass

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
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
import com.example.classscheduler.core.utils.ext.toLocalTime
import com.example.classscheduler.ui.shared.SingleSelectDropDown
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import com.example.classscheduler.domain.models.Class;
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter

@Serializable
object DeleteClassRoute

@Composable
fun DeleteClassScreen(
    openManageClass : () -> Unit,
    showSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    deleteClassViewModel: DeleteClassViewModel = hiltViewModel()
):Unit{
    val state by deleteClassViewModel.state.collectAsStateWithLifecycle();

    deleteClassViewModel.events.ObserveEventsAs { event ->
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

    DeleteClassScreenContent(
        state = state,
        onNavigateToManageClasses = { deleteClassViewModel.onIntent(DeleteClassIntent.OnNavigateToManageClasses) },
        onDeleteClass = { deleteClassViewModel.onIntent(DeleteClassIntent.OnDeleteClass) },
        onClassSelected = { selectedClass -> deleteClassViewModel.onIntent(DeleteClassIntent.OnClassSelected(selectedClass)) },
        modifier = modifier
    );
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteClassScreenContent(
    state: DeleteClassState,
    onClassSelected: (Class?) -> Unit,
    onDeleteClass: () -> Unit,
    onNavigateToManageClasses: () -> Unit,
    modifier: Modifier = Modifier
):Unit{
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.delete_class_title)) },
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
    ){ paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                when{
                    state.isLoading -> LoadingIndicator()
                    state.classes.isNullOrEmpty() -> EmptyClasses()
                    else -> DeleteClassContentSection(
                        state,
                        onDeleteClass = onDeleteClass,
                        onClassSelected = onClassSelected
                    )
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
            text = "Loading classes...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DeleteClassContentSection(
    state: DeleteClassState,
    onClassSelected: (Class?) -> Unit,
    onDeleteClass: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Select the class to delete",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                SingleSelectDropDown(
                    label = "Select class",
                    options = state.classes ?: emptyList(),
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
                    ClassInfoPreview(classItem)
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = onDeleteClass,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.delete_class_action))
        }
    }
}

@Composable
fun ClassInfoPreview(classItem: Class) {
  Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = classItem.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Teacher: ${classItem.teacher}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Classroom: ${classItem.classroom}",
                style = MaterialTheme.typography.bodyMedium
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                classItem.schedule.forEach { schedule ->
                    Text(
                        text = "📅 ${schedule.day} • " +
                                "${schedule.startTimeLong?.toLocalTime()?.format(DateTimeFormatter.ofPattern("hh:mm a"))?: "--"} - " +
                                (schedule.endTimeLong?.toLocalTime()?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "--"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
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

@Preview(showSystemUi = true)
@Composable
fun DeleteClassScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        DeleteClassScreenContent(
            state = DeleteClassState(),
            onNavigateToManageClasses = {},
            onDeleteClass = {},
            onClassSelected = {}
        );
    }
}