package com.fwrdgrp.financetracker.ui.uiutils

import com.fwrdgrp.financetracker.data.datautils.deriveDateFields
import com.fwrdgrp.financetracker.data.enum.BarData
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.fwrdgrp.financetracker.data.model.ui.BarChartData
import com.fwrdgrp.financetracker.data.model.ui.PieChartData
import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.ceil

fun getDayWithSuffix(day: Int): String {
    return when {
        day in 11..13 -> "${day}th"
        day % 10 == 1 -> "${day}st"
        day % 10 == 2 -> "${day}nd"
        day % 10 == 3 -> "${day}rd"
        else -> "${day}th"
    }
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

fun createFormWithToday(): TransactionReq {
    val calendar = Calendar.getInstance()
    val timestamp = Timestamp(calendar.time)
    val derivedDate = deriveDateFields(calendar.timeInMillis)
    return TransactionReq(
        timestamp = timestamp,
        year = derivedDate.year,
        month = derivedDate.month,
        day = derivedDate.day,
        week = derivedDate.week
    )
}

fun List<Transaction>.toPieChartData(): List<PieChartData> {
    val expenses = this.filter { it.type == TransactionType.Expense }
    val categoryTotals = expenses
        .groupBy { it.category }
        .mapValues { (_, transactions) ->
            transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }.toFloat()
        }
        .filter { it.value > 0 }

    return categoryTotals.map { (category, total) ->
        PieChartData(
            label = category.name,
            value = total,
            color = category.color
        )
    }.sortedByDescending { it.value }
}

val months = arrayOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

fun Calendar.toFullTextDate(): String {
    return "${getDayWithSuffix(this.get(Calendar.DAY_OF_MONTH))} of ${
        months[this.get(Calendar.MONTH)]
    }, ${this.get(Calendar.YEAR)}"
}

