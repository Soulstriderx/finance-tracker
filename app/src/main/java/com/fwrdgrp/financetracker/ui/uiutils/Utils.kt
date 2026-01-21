package com.fwrdgrp.financetracker.ui.uiutils

import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

fun getDayWithSuffix(day: Int): String {
    return when {
        day in 11..13 -> "${day}th"
        day % 10 == 1 -> "${day}st"
        day % 10 == 2 -> "${day}nd"
        day % 10 == 3 -> "${day}rd"
        else -> "${day}th"
    }
}

fun String.toDisplayName(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

fun Double.withCommas(): String {
    return NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(this)
}

fun Timestamp.toCalendar(): Calendar =
    Calendar.getInstance().apply {
        time = this@toCalendar.toDate()
    }

fun Calendar.toTimestamp(): Timestamp =
    Timestamp(this.time)