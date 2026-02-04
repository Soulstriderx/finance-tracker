package com.fwrdgrp.financetracker.ui.composables.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.data.datautils.deriveDateFields
import com.fwrdgrp.financetracker.data.model.request.BillReq
import com.fwrdgrp.financetracker.ui.composables.general.MediumTitleText
import com.fwrdgrp.financetracker.ui.composables.input.CustomTextField
import com.fwrdgrp.financetracker.ui.composables.input.DatePicker
import com.fwrdgrp.financetracker.ui.theme.MutedBrown
import com.fwrdgrp.financetracker.ui.theme.OffWhite
import com.fwrdgrp.financetracker.ui.uiutils.toCalendar
import com.fwrdgrp.financetracker.ui.uiutils.toRegisterString
import com.google.firebase.Timestamp

@Composable
fun ManageBillDialog(
    title: String = "Add Bill",
    isEdit: Boolean = false,
    form: BillReq,
    showDateDialog: Boolean,
    onDateDialogChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (BillReq) -> Unit,
) {
    var form by remember { mutableStateOf(form) }
    var timestampCalendar by remember(form.nextDue) {
        mutableStateOf(
            if (isEdit) form.newNextDue?.toCalendar() else
                form.nextDue?.toCalendar()
        )
    }

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { onDateDialogChange(false) },
            confirmButton = {
                TextButton(onClick = { onDateDialogChange(false) }) {
                    Text("OK")
                }
            },
            content = { Text("Select Date") },
        )
    }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(title)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                MediumTitleText("Name")
                CustomTextField(
                    "Name", if (isEdit) form.newName else form.name,
                    modifier = Modifier.background(
                        color = OffWhite,
                        shape = RoundedCornerShape(12.dp)
                    )
                )
                { form = if (isEdit) form.copy(newName = it) else form.copy(name = it) }
                Spacer(Modifier.height(8.dp))
                MediumTitleText("Amount")
                CustomTextField(
                    "Amount", if (isEdit) form.newAmount else form.amount,
                    modifier = Modifier.background(
                        color = OffWhite,
                        shape = RoundedCornerShape(12.dp)
                    )
                )
                { form = if (isEdit) form.copy(newAmount = it) else form.copy(amount = it) }
                Spacer(Modifier.height(8.dp))
                MediumTitleText("Recurring Day")
                DatePicker(
                    selectedDate = timestampCalendar?.toRegisterString() ?: "",
                    existingCalendar = timestampCalendar,
                    onDateSelected = { calendar ->
                        val timestamp = Timestamp(calendar.time)
                        val derivedDate = deriveDateFields(calendar.timeInMillis)
                        timestampCalendar = calendar
                        form = if (isEdit) form.copy(
                            newNextDue = timestamp,
                            newDay = derivedDate.day,
                        ) else form.copy(
                            nextDue = timestamp,
                            day = derivedDate.day,
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onConfirm(form)
                }
            ) {
                Text(if (isEdit) "Update" else "Add")
            }
        },
        dismissButton = {
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MutedBrown,
                    contentColor = OffWhite
                )
            ) {
                Text("Cancel")
            }
        }
    )
}