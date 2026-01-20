package com.fwrdgrp.financetracker.ui.composables

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
import com.fwrdgrp.financetracker.data.model.ui.FieldData

@Composable
fun CustomTextField(
    field: FieldData
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = field.value,
        onValueChange = field.onValueChange,
        shape = RoundedCornerShape(8.dp),
        visualTransformation = if(field.isPassword) {
            if (!visible) PasswordVisualTransformation()
            else VisualTransformation.None
        } else VisualTransformation.None,
        trailingIcon = {
            if (field.isPassword) {
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
        label = { Text(field.label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}