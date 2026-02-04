package com.fwrdgrp.financetracker.ui.composables.home

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.ui.uiutils.withCommas

@Composable
fun IncomeDialog(user: User, onDismiss: () -> Unit, onSuccess: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text("Monthly Income")
        },
        text = {
            Text(
                "Have you received your monthly income of $${
                    user.monthlyIncome.amount.toDouble().withCommas()
                } today?"
            )
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onSuccess()
                    onDismiss()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Not yet")
            }
        }
    )
}