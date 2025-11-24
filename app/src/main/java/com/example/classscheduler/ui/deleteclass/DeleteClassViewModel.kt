package com.example.classscheduler.ui.deleteclass

import androidx.lifecycle.viewModelScope
import com.example.classscheduler.core.common.BaseViewModel
import com.example.classscheduler.core.ui.Screen
import com.example.classscheduler.R;
import com.example.classscheduler.core.ui.UiEvent.*
import com.example.classscheduler.core.ui.UiText
import com.example.classscheduler.core.utils.ext.match
import com.example.classscheduler.core.utils.validation.validators.DeleteClassValidator
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.interfaces.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteClassViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val classRepository: ClassRepository
) : BaseViewModel<DeleteClassIntent, DeleteClassState>() {
    private val _state: MutableStateFlow<DeleteClassState> = MutableStateFlow(DeleteClassState());

    val state = _state.asStateFlow();

    init {
        addValidator(DeleteClassValidator());
        loadClasses();
    }

    override fun onIntent(intent: DeleteClassIntent) {
        when(intent){
            is DeleteClassIntent.OnClassSelected -> {
                _state.update { currentState -> currentState.copy(selectedClass = intent.selectedClass) }
            }
            DeleteClassIntent.OnNavigateToManageClasses -> {
                viewModelScope.launch {
                    channel.send(Navigate(Screen.ManageClasses));
                }
            }
            DeleteClassIntent.OnDeleteClass -> {
                val validationResult = validator!!.validate(_state.value);

                _state.update { currentState ->
                    currentState.copy(
                        selectedClassHasError = validationResult["selected-class"]?.errorMessage
                    )
                };

                if(!validationResult.values.all { it.isValid }) return;

                viewModelScope.launch {
                    _state.update { currentState -> currentState.copy(isLoading = true) }

                    val result = classRepository.delete(_state.value.selectedClass!!.id);

                    result.match(
                        onSuccess = {
                            channel.send(ShowSnackBar(UiText.StringResource(R.string.class_deleted)));
                            _state.update { currentState -> currentState.copy(selectedClass = null) }
                            loadClasses();
                        },
                        onFailure = { error ->
                            channel.send(ShowSnackBar(error.message))
                        }
                    )

                    _state.update { currentState -> currentState.copy(isLoading = false) }
                }
            }
        }
    }

    private fun loadClasses():Unit{
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true) };

            val result = classRepository.getClassesByOwnerId(authRepository.currentUser?.uid!!);

            result.match(
                onSuccess = { classes ->
                    _state.update { currentState -> currentState.copy(classes = classes) }
                },
                onFailure = {error ->
                    channel.send(ShowSnackBar(error.message));
                }
            )

            _state.update { currentState -> currentState.copy(isLoading = false) };
        }
    }
}