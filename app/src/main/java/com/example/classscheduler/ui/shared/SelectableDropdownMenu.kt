package com.example.classscheduler.ui.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectableDropdownMenu(
    label: String = "Select item(s)",
    options: List<T>,
    itemLabel: (T) -> String,
    selected: List<T>,
    onSelectionChange: (List<T>) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) };

    val selectedItems = selected.toMutableStateList();

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            value = selectedItems.joinToString(", ") { itemLabel(it) },
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(text = label) },
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
            if (options.size > 10) {
                LazyColumn {
                    items(options) { item ->
                        DropdownOption(
                            item,
                            selectedItems,
                            itemLabel,
                            onSelectionChange
                        );
                    }
                    return@LazyColumn;
                }
            }
            options.forEach { option ->
                DropdownOption(
                    option,
                    selectedItems,
                    itemLabel,
                    onSelectionChange
                );
            }
        }
    }
}

@Composable
private fun <T> DropdownOption(
    option: T,
    selectedItems: MutableList<T>,
    itemLabel: (T) -> String,
    onSelectionChange: (List<T>) -> Unit
) {
    val isSelected = selectedItems.contains(option);

    DropdownMenuItem(
        text = { Text(itemLabel(option)) },
        onClick = {
            if (isSelected) {
                selectedItems.remove(option);
            } else {
                selectedItems.add(option);
            }
            // Sending a copy of the list
            onSelectionChange(selectedItems.toList());
        },
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon"
                )
            }
        }
    )
}