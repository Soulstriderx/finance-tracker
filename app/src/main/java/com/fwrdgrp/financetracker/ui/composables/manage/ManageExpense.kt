package com.fwrdgrp.financetracker.ui.composables.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fwrdgrp.financetracker.data.datautils.deriveDateFields
import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.enum.PaymentMethod
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.fwrdgrp.financetracker.ui.composables.general.TabButtons
import com.fwrdgrp.financetracker.ui.composables.input.CustomDropdown
import com.fwrdgrp.financetracker.ui.composables.input.CustomTextField
import com.fwrdgrp.financetracker.ui.composables.input.DatePicker
import com.fwrdgrp.financetracker.ui.uiutils.createFormWithToday
import com.fwrdgrp.financetracker.ui.uiutils.toCalendar
import com.fwrdgrp.financetracker.ui.uiutils.toFullTextDate
import com.google.firebase.Timestamp

@Composable
fun ManageExpense(
    newForm: TransactionReq,
    showDialog: Boolean,
    onDialogChange: (Boolean) -> Unit,
    isEditing: Boolean = false,
    onCancel: () -> Unit,
    onSubmit: (TransactionReq) -> Unit,
) {
    var form by remember { mutableStateOf(newForm) }

    val typeTabs = TransactionType.entries
    val tabs = Category.entries

    var timestampCalendar by remember(form.timestamp) {
        mutableStateOf(form.timestamp?.toCalendar())
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { onDialogChange(false) },
            confirmButton = {
                TextButton(onClick = { onDialogChange(false) }) {
                    Text("OK")
                }
            },
            content = { Text("Select Date") },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Transaction Type",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
            ) {
                TabButtons(
                    typeTabs,
                    form.type,
                    label = { it.toString() }
                ) { form = form.copy(type = it) }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Amount",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            CustomTextField("Amount", form.amount)
            { form = form.copy(amount = it) }

            Spacer(Modifier.height(16.dp))
            Text(
                "Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
            ) {
                TabButtons(
                    tabs,
                    form.category,
                    isIconButton = true,
                    iconLabel = { it.icon },
                    colorLabel = { it.color },
                ) { form = form.copy(category = it) }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            DatePicker(
                selectedDate = timestampCalendar?.toFullTextDate() ?: "",
                existingCalendar = timestampCalendar,
                onDateSelected = { calendar ->
                    val timestamp = Timestamp(calendar.time)
                    val derivedDate = deriveDateFields(calendar.timeInMillis)
                    timestampCalendar = calendar
                    form = form.copy(
                        timestamp = timestamp,
                        year = derivedDate.year,
                        month = derivedDate.month,
                        day = derivedDate.day,
                        week = derivedDate.week
                    )
                }
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Note",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            CustomTextField("Note", form.note)
            { form = form.copy(note = it) }
            Spacer(Modifier.height(16.dp))
            Text(
                "Payment Method",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            CustomDropdown(
                items = PaymentMethod.entries,
                selectedItem = form.method.name,
                itemLabel = { it.name })
            {
                form = form.copy(method = it)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { onSubmit(form) }
            ) {
                Text(
                    "Save Transaction",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(40.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { onCancel() }
            ) {
                Text(
                    "Cancel",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}