package com.example.classscheduler.ui.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SingleSelectDropDown(
    label: String = "Select an item",
    options: List<T>,
    itemLabel: (T) -> String,
    onSelection: (T?) -> Unit,
): Unit{
    var isExpanded by remember { mutableStateOf(false) };

    var selectedItem by remember { mutableStateOf<T?>(null) };

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            value = selectedItem?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            if(options.size < 10){
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(itemLabel(option)) },
                        onClick = {
                            selectedItem = option
                            isExpanded = false
                            onSelection(option)
                        }
                    )
                }

                return@ExposedDropdownMenu;
            }

            LazyColumn {
                items(options){ option ->
                    DropdownMenuItem(
                        text = { Text(itemLabel(option)) },
                        onClick = {
                            selectedItem = option
                            isExpanded = false
                            onSelection(option)
                        }
                    )
                }
            }
        }
    }
}