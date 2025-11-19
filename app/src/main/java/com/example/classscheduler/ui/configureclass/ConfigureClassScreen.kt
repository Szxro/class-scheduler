package com.example.classscheduler.ui.configureclass

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
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
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import com.example.classscheduler.R;
import com.example.classscheduler.core.utils.ext.toLocalTime
import com.example.classscheduler.ui.shared.SingleSelectDropDown
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter

@Serializable
object ConfigureClassRoute


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ConfigureClassScreen(
    onNavigateToManageClass: () -> Unit,
    showSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    configureClassViewModel: ConfigureClassViewModel = hiltViewModel(),
): Unit{
    val state by configureClassViewModel.state.collectAsStateWithLifecycle();

    configureClassViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate -> {
                when(event.destination){
                    Screen.ManageClasses -> onNavigateToManageClass()
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar -> showSnackBar(event.message)
        }
    }

    ConfigureClassScreenContent(
        state= state,
        onNavigateToManageClass = { configureClassViewModel.onIntent(ConfigureClassIntent.OnNavigateToManageClass) },
        onClassSelected = { selectedClass -> configureClassViewModel.onIntent(ConfigureClassIntent.OnSelectedClassChange(selectedClass)) },
        onConfigure = { configureClassViewModel.onIntent(ConfigureClassIntent.OnConfigure) },
        onCancel = { configureClassViewModel.onIntent(ConfigureClassIntent.OnCancel) },
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigureClassScreenContent(
    state: ConfigureClassState,
    onNavigateToManageClass: () -> Unit,
    onClassSelected: (Class) -> Unit,
    onConfigure: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
): Unit{
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.configure_class_title)) },
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
                    state.classes.isEmpty() -> EmptyClasses()
                    else -> ConfigureClassContentSection(
                        state,
                        onClassSelected,
                        onConfigure,
                        onCancel
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun ConfigureClassContentSection(
    state: ConfigureClassState,
    onClassSelected: (Class) -> Unit,
    onConfigure: () -> Unit,
    onCancel: () -> Unit
): Unit{
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        val isConfigured = state.selectedClass?.configured == true;

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
                    text = "Select the class to configure",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                SingleSelectDropDown(
                    label = "Select class",
                    options = state.classes,
                    itemLabel = { classes -> classes.name },
                    onSelection = { onClassSelected(it!!) }
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
            onClick = if(isConfigured) onCancel else  onConfigure,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = if (isConfigured) Icons.Default.Cancel else Icons.Default.CheckCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(text = if (isConfigured) stringResource(R.string.cancel_class_action) else stringResource(R.string.configure_class_action))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
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
private fun LoadingIndicator(): Unit {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Loading...",
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
fun ConfigureClassScreenPreview(): Unit{
    ClassSchedulerTheme(darkTheme = true){
        ConfigureClassScreenContent(
            state = ConfigureClassState(),
            onConfigure = {},
            onClassSelected = {selectedClass -> },
            onNavigateToManageClass = {},
            onCancel = {}
        )
    }
}