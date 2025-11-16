package com.example.classscheduler.ui.manageclasses

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.core.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassesViewModel @Inject constructor()
    : BaseViewModel<ManageClassIntent, Nothing>()  {

    override fun onIntent(intent: ManageClassIntent) {
        when(intent){
            ManageClassIntent.OnNavigateToHome -> {
                viewModelScope.launch {
                    channel.send(UiEvent.Navigate(Screen.Home))
                }
            }
            ManageClassIntent.OnNavigateToCreateClass -> {
                viewModelScope.launch {
                    channel.send(UiEvent.Navigate(Screen.CreateClass))
                }
            }
            ManageClassIntent.OnNavigateToUpdateClass -> {
                viewModelScope.launch {
                    channel.send(UiEvent.Navigate(Screen.UpdateClass))
                }
            }
            ManageClassIntent.OnNavigateToDeleteClass -> {
                viewModelScope.launch {
                    channel.send(UiEvent.Navigate(Screen.DeleteClass))
                }
            }
            ManageClassIntent.OnNavigateToConfigureClass -> {
                viewModelScope.launch {
                    channel.send(UiEvent.Navigate(Screen.ConfigureClass))
                }
            }
        }
    }
}