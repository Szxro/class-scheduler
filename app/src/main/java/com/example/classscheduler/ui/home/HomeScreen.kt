package com.example.classscheduler.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.layout.ContentScale
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

@Serializable
object HomeRoute;

@Composable
fun HomeScreen(
    openSignInScreen : () -> Unit,
    showSnackBar: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
):Unit{
    val state by homeViewModel.state.collectAsStateWithLifecycle();

    homeViewModel.events.ObserveEventsAs { event ->
        when(event){
            is UiEvent.Navigate -> {
                when(event.destination){
                    Screen.SignIn -> openSignInScreen()
                    else -> Unit
                }
            }
            is UiEvent.ShowSnackBar ->  showSnackBar(event.message)
        }
    }
    HomeScreenContent(
        state,
        onLogoutClicked = { homeViewModel.onIntent(HomeIntent.OnLogoOut) },
        modifier
    );
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    state: HomeState,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

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
                                    contentDescription = "Profile Picture",
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
                            } ?:
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Default Profile Picture",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(90.dp)
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = state.currentUser?.email ?: "Unknown Email",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    NavigationDrawerItem(
                        label = { Text("Create Class") },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Class"
                            )
                        },
                        onClick = {
                            // TODO: OPEN DIALOG WITH THE FORM TO CREATE A CLASS (name, code, teacher, classroom, days and times)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        "Classes by Day",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )

                    listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday").forEach { day ->
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
                                // TODO: OPEN A DIALOG WITH INFO ABOUT CLASSES IN THAT DAY
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    NavigationDrawerItem(
                        label = { Text("Logout") },
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
                                        if(isClosed) open() else close();
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
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TODO: CREATE CARD BASE IN THE DAY (() -> OPEN A DIALOG WITH INFO ABOUT THE CLASSES IN THAT DAY)

                    Text(
                        "Welcome, ${state.currentUser?.email ?: "User"}!",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview():Unit{
    ClassSchedulerTheme(darkTheme = true){
        HomeScreenContent(
            state = HomeState(),
            onLogoutClicked = {},
        );
    }
}
