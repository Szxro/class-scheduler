package com.example.classscheduler.ui.shared

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.classscheduler.core.utils.ext.toEpochMilli
import com.example.classscheduler.core.utils.ext.toLocalTime
import com.example.classscheduler.domain.models.Schedule
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleRow(
    schedule: Schedule,
    onTimeChange: (Long?, Long?) -> Unit
) {
    var showStartPicker by remember { mutableStateOf(false) }

    var showEndPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = schedule.day, style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TimeButton(
                label = "Start",
                time = schedule.startTimeLong?.toLocalTime(),
                onClick = { showStartPicker = true }
            )
            TimeButton(
                label = "End",
                time = schedule.endTimeLong?.toLocalTime(),
                onClick = { showEndPicker = true }
            )
        }

        if (showStartPicker) {
            TimePicker(
                onDismiss = { showStartPicker = false },
                onConfirm = { state ->
                    val selectedTime = LocalTime.of(state.hour, state.minute);
                    onTimeChange(selectedTime.toEpochMilli(), schedule.endTimeLong)
                    showStartPicker = false;
                }
            )
        }

        if (showEndPicker) {
            TimePicker(
                onDismiss = { showEndPicker = false },
                onConfirm = { state ->
                    val selectedTime = LocalTime.of(state.hour, state.minute);
                    onTimeChange(schedule.startTimeLong,selectedTime.toEpochMilli());
                    showEndPicker = false;
                }
            )
        }
    }
}

@Composable
fun TimeButton(label: String, time: LocalTime? = null, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "$label: ${time?.toString() ?: "--:--"}");
    }
}