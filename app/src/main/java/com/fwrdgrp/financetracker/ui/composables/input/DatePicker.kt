package com.fwrdgrp.financetracker.ui.composables.input

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@SuppressLint("RememberInComposition")
@Composable
fun DatePicker(
    selectedDate: String,
    existingCalendar: Calendar?,
    onDateSelected: (Calendar) -> Unit
) {
    val context = LocalContext.current
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Select Day") },
        trailingIcon = { /*Empty trailing icon makes it fully clickable*/ },
        interactionSource = MutableInteractionSource().apply {
            LaunchedEffect(this) {
                interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) {
                        val calendar = existingCalendar ?: Calendar.getInstance()

                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val cal = existingCalendar ?: Calendar.getInstance()
                                cal.set(year, month, dayOfMonth)
                                onDateSelected(cal)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }
            }
        }
    )
}
