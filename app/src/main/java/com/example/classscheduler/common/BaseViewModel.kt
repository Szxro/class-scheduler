package com.example.classscheduler.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classscheduler.domain.models.ErrorMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import com.example.classscheduler.R;
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


open class BaseViewModel : ViewModel() {
    /*
    * Generic coroutine error management
    * */
    fun launchCatching(
        showErrorDialog: (ErrorMessage) -> Unit = {},
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit
    ) : Job =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                val error = throwable.message?.let { message ->
                    ErrorMessage.StringError(message)
                } ?: ErrorMessage.IdError(R.string.generic_error);

                showErrorDialog(error)
            } + dispatcher,
            block = block
        )
}