package com.fwrdgrp.financetracker.ui.composables.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    label: String = "",
    value: String,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if(isPassword) {
            if (!visible) PasswordVisualTransformation()
            else VisualTransformation.None
        } else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                Icon(
                    imageVector = if (visible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        visible = !visible
                    }
                )
            }
        },
        placeholder = { Text(label) },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}