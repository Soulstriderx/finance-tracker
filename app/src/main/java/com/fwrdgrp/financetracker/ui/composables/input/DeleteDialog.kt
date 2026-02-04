package com.fwrdgrp.financetracker.ui.composables.input

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.ui.theme.MutedBrown
import com.fwrdgrp.financetracker.ui.theme.OffWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    showDialog: Boolean,
    title: String,
    onDismiss: (Boolean) -> Unit,
    onSuccess: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            shape = RoundedCornerShape(8.dp),
            onDismissRequest = { onDismiss(false) },
            title = { Text(title) },
            text = { Text("Are you sure? This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = { onSuccess() },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss(false) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MutedBrown,
                        contentColor = OffWhite
                    )
                ) {
                    Text("No")
                }
            }
        )
    }
}