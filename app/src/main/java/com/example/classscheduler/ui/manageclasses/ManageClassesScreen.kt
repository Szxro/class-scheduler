package com.example.classscheduler.ui.manageclasses

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import com.example.classscheduler.core.utils.ext.ObserveEventsAs
import com.example.classscheduler.ui.theme.ClassSchedulerTheme
import kotlinx.serialization.Serializable

@Serializable
object ManageClassesRoute

@Composable
fun ManageClassesScreen(
    openHomeScreen: () -> Unit,
    openCreateClassScreen: () -> Unit,
    openUpdateClassScreen: () -> Unit,
    openDeleteClassScreen: () -> Unit,
    openConfigureClassScreen: () -> Unit,
    modifier: Modifier = Modifier,
    manageClassesViewModel: ManageClassesViewModel = hiltViewModel(),
): Unit {

    manageClassesViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate -> {
                when(event.destination){
                    Screen.Home -> openHomeScreen()
                    Screen.CreateClass -> openCreateClassScreen()
                    Screen.UpdateClass -> openUpdateClassScreen()
                    Screen.DeleteClass -> openDeleteClassScreen()
                    Screen.ConfigureClass -> openConfigureClassScreen()
                    else -> Unit
                }
            }
            else -> Unit
        }
    }

    ManageClassesScreenContent(
        onNavigateToHome = {
            manageClassesViewModel.onIntent(ManageClassIntent.OnNavigateToHome)
        },
        onNavigateToCreateClass = {
            manageClassesViewModel.onIntent(ManageClassIntent.OnNavigateToCreateClass)
        },
        onNavigateToUpdateClass = {
            manageClassesViewModel.onIntent(ManageClassIntent.OnNavigateToUpdateClass)
        },
        onNavigateToDeleteClass = {
            manageClassesViewModel.onIntent(ManageClassIntent.OnNavigateToDeleteClass)
        },
        onNavigateToConfigureClass = {
            manageClassesViewModel.onIntent(ManageClassIntent.OnNavigateToConfigureClass)
        },
        modifier
    )
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageClassesScreenContent(
    onNavigateToHome: () -> Unit,
    onNavigateToCreateClass: () -> Unit,
    onNavigateToUpdateClass: () -> Unit,
    onNavigateToDeleteClass: () -> Unit,
    onNavigateToConfigureClass: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Manage Classes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) {  paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select an action",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ManageButton(
                        text = "Create",
                        icon = Icons.Default.Add,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToCreateClass
                    )
                    ManageButton(
                        text = "Update",
                        icon = Icons.Default.Edit,
                        color = MaterialTheme.colorScheme.tertiary,
                        onClick = onNavigateToUpdateClass
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ManageButton(
                        text = "Delete",
                        icon = Icons.Default.Delete,
                        color = MaterialTheme.colorScheme.error,
                        onClick = onNavigateToDeleteClass
                    )
                    ManageButton(
                        text = "Configure",
                        icon = Icons.Default.Settings,
                        color = MaterialTheme.colorScheme.secondary,
                        onClick = onNavigateToConfigureClass
                    )
                }
            }
        }
    }
}

@Composable
private fun ManageButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .size(width = 140.dp, height = 120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ManageClassesScreenPreview(): Unit {
    ClassSchedulerTheme(darkTheme = true) {
        ManageClassesScreenContent(
            onNavigateToHome = {},
            onNavigateToCreateClass = {},
            onNavigateToUpdateClass = {},
            onNavigateToDeleteClass = {},
            onNavigateToConfigureClass = {}
        )
    }
}