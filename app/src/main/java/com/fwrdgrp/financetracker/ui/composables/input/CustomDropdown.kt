package com.fwrdgrp.financetracker.ui.composables.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.ui.theme.OffWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CustomDropdown(
    items: List<T>,
    selectedItem: String,
    enabled: Boolean = true,
    itemLabel: (T) -> String,
    onDeleteItem: ((T) -> Unit)? = null,
    onSelectedChange: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (items.isNotEmpty() && enabled) expanded = !expanded },
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = if (items.isNotEmpty()) Color.Black else Color.Gray,
                unfocusedTextColor = if (items.isNotEmpty()) Color.Black else Color.Gray,
                focusedContainerColor = OffWhite,
                unfocusedContainerColor = OffWhite
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .background(Color.White, RoundedCornerShape(12.dp))
        )

        ExposedDropdownMenu(
            modifier = Modifier.background(color = Color.White),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(itemLabel(item))
                            if (onDeleteItem != null) {
                                IconButton(
                                    onClick = {
                                        onDeleteItem(item)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Delete ${itemLabel(item)}",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    },
                    onClick = {
                        onSelectedChange(item)
                        expanded = false
                    },
                    modifier = Modifier.background(Color.White),
                )
                HorizontalDivider(thickness = 1.dp)
            }

        }
    }
}