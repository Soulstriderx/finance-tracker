package com.fwrdgrp.financetracker.ui.composables.input

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.ui.theme.OffWhite
import java.util.Calendar

@SuppressLint("RememberInComposition")
@Composable
fun DatePicker(
    selectedDate: String,
    existingCalendar: Calendar?,
    modifier: Modifier = Modifier,
    onDateSelected: (Calendar) -> Unit
) {
    val context = LocalContext.current
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .background(color = OffWhite, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth(),
        placeholder = { Text("Select Day") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date"
            )
        },
        interactionSource = remember { MutableInteractionSource() }.apply {
            LaunchedEffect(this) {
                interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) {
                        val calendar = existingCalendar ?: Calendar.getInstance()
                        val minDate = Calendar.getInstance().apply {
                            set(2020, Calendar.JANUARY, 1)
                        }
                        val maxDate = Calendar.getInstance().apply {
                            add(Calendar.MONTH, 2)
                        }

                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val cal = Calendar.getInstance().apply {
                                    set(year, month, dayOfMonth)
                                }
                                onDateSelected(cal)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.datePicker.minDate = minDate.timeInMillis
                        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis
                        datePickerDialog.show()
                    }
                }
            }
        }
    )
}
