package com.fwrdgrp.financetracker.ui.composables.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.fwrdgrp.financetracker.ui.composables.input.CustomTextField

@Composable
fun TextValueRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isInput: Boolean = false,
    formValue: String = "",
    isOver: Boolean = false,
    onValueChange: ((String) -> Unit)? = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, Modifier.weight(1f), color = if (isOver) Color.Red else Color.Black)
        if (isInput) {
            CustomTextField(value = formValue, modifier = Modifier.weight(1f)) {
                onValueChange?.invoke(it)
            }
        } else {
            Text(value, color = if (isOver) Color.Red else Color.Black)
        }
    }
}