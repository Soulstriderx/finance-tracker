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

fun Calendar.toWeekRangeText(): String {
    val endDate = this.clone() as Calendar
    val startDate = this.clone() as Calendar
    startDate.add(Calendar.DAY_OF_MONTH, -7)

    val startDay = getDayWithSuffix(startDate.get(Calendar.DAY_OF_MONTH))
    val startMonth = months[startDate.get(Calendar.MONTH)]
    val endDay = getDayWithSuffix(endDate.get(Calendar.DAY_OF_MONTH))
    val endMonth = months[endDate.get(Calendar.MONTH)]
    val year = endDate.get(Calendar.YEAR)

    return if (startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH)) {
        "$startDay to $endDay of $endMonth, $year"
    } else {
        val startYear = startDate.get(Calendar.YEAR)
        if (startYear == year) {
            "$startDay of $startMonth to $endDay of $endMonth, $year"
        } else {
            "$startDay of $startMonth, $startYear to $endDay of $endMonth, $year"
        }
    }
}

fun Calendar.toMonthRangeText(): String {
    val endDate = this.clone() as Calendar
    val startDate = this.clone() as Calendar
    startDate.add(Calendar.DAY_OF_MONTH, -30)

    val startDay = getDayWithSuffix(startDate.get(Calendar.DAY_OF_MONTH))
    val startMonth = months[startDate.get(Calendar.MONTH)]
    val endDay = getDayWithSuffix(endDate.get(Calendar.DAY_OF_MONTH))
    val endMonth = months[endDate.get(Calendar.MONTH)]
    val year = endDate.get(Calendar.YEAR)

    return if (startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH)) {
        "$startDay to $endDay of $endMonth, $year"
    } else {
        val startYear = startDate.get(Calendar.YEAR)
        if (startYear == year) {
            "$startDay of $startMonth to $endDay of $endMonth, $year"
        } else {
            "$startDay of $startMonth, $startYear to $endDay of $endMonth, $year"
        }
    }
}

fun Calendar.toRegisterString(): String {
    return "${getDayWithSuffix(this.get(Calendar.DAY_OF_MONTH))} of every month"
}

fun getDailyLabels(referenceCalendar: Calendar = Calendar.getInstance()): List<String> {
    val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val calendar = referenceCalendar.clone() as Calendar
    val labels = mutableListOf<String>()

    // Go back 6 days from reference date
    for (i in 6 downTo 0) {
        calendar.add(Calendar.DAY_OF_YEAR, -i)
        labels.add(dateFormat.format(calendar.time))
        calendar.add(Calendar.DAY_OF_YEAR, i) // Reset
    }
    return labels
}

fun getWeeklyLabels(referenceCalendar: Calendar = Calendar.getInstance()): List<String> {
    val labels = mutableListOf<String>()
    val currentWeek = referenceCalendar.get(Calendar.WEEK_OF_YEAR)

    // Go back 3 weeks from reference date
    for (i in 3 downTo 0) {
        val weekNum = currentWeek - i
        labels.add("W$weekNum")
    }
    return labels
}

fun getMonthlyLabels(referenceCalendar: Calendar = Calendar.getInstance()): List<String> {
    val dateFormat = SimpleDateFormat("MMM", Locale.getDefault())
    val calendar = referenceCalendar.clone() as Calendar
    val labels = mutableListOf<String>()

    // Go back 5 months from reference date
    for (i in 5 downTo 0) {
        calendar.add(Calendar.MONTH, -i)
        labels.add(dateFormat.format(calendar.time))
        calendar.add(Calendar.MONTH, i) // Reset
    }
    return labels
}

fun aggregateDailyTransactions(
    transactions: List<Transaction>,
    referenceCalendar: Calendar = Calendar.getInstance()
): List<BarData> {
    val labels = getDailyLabels(referenceCalendar)
    val dailyTotals = mutableMapOf<String, Float>()

    labels.forEach { dailyTotals[it] = 0f }

    transactions.forEach { transaction ->
        transaction.timestamp?.toDate()?.let { date ->
            val transCalendar = Calendar.getInstance().apply { time = date }
            val daysDiff = ((referenceCalendar.timeInMillis - transCalendar.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

            if (daysDiff in 0..6) {
                val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val dayLabel = dateFormat.format(date)
                dailyTotals[dayLabel] = (dailyTotals[dayLabel] ?: 0f) + transaction.amount.toFloat()
            }
        }
    }

    return labels.map { BarData(it, dailyTotals[it] ?: 0f) }
}

fun aggregateWeeklyTransactions(
    transactions: List<Transaction>,
    referenceCalendar: Calendar = Calendar.getInstance()
): List<BarData> {
    val labels = getWeeklyLabels(referenceCalendar)
    val currentWeek = referenceCalendar.get(Calendar.WEEK_OF_YEAR)
    val weeklyTotals = mutableMapOf<String, Float>()

    labels.forEach { weeklyTotals[it] = 0f }

    transactions.forEach { transaction ->
        val weekLabel = "W${transaction.week}"
        if (transaction.week in (currentWeek - 3)..currentWeek) {
            weeklyTotals[weekLabel] = (weeklyTotals[weekLabel] ?: 0f) + transaction.amount.toFloat()
        }
    }

    return labels.map { BarData(it, weeklyTotals[it] ?: 0f) }
}

fun aggregateMonthlyTransactions(
    transactions: List<Transaction>,
    referenceCalendar: Calendar = Calendar.getInstance()
): List<BarData> {
    val labels = getMonthlyLabels(referenceCalendar)
    val monthlyTotals = mutableMapOf<String, Float>()

    labels.forEach { monthlyTotals[it] = 0f }

    transactions.forEach { transaction ->
        val transCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, transaction.year)
            set(Calendar.MONTH, transaction.month - 1)
        }
        val dateFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val monthLabel = dateFormat.format(transCalendar.time)

        monthlyTotals[monthLabel] = (monthlyTotals[monthLabel] ?: 0f) + transaction.amount.toFloat()
    }

    return labels.map { BarData(it, monthlyTotals[it] ?: 0f) }
}


fun calculateChartData(
    selectedTab: DateFilter,
    transactions: List<Transaction>,
    referenceCalendar: Calendar = Calendar.getInstance()
): BarChartData {
    val data = when (selectedTab) {
        DateFilter.Daily -> aggregateDailyTransactions(transactions, referenceCalendar)
        DateFilter.Weekly -> aggregateWeeklyTransactions(transactions, referenceCalendar)
        DateFilter.Monthly -> aggregateMonthlyTransactions(transactions, referenceCalendar)
    }

    val maxSpend = data.maxOfOrNull { it.spend } ?: 0f
    val average = if (data.isNotEmpty()) {
        data.map { it.spend }.sum() / data.size
    } else 0f

    val maxValue = (ceil(maxSpend / 100) * 100 + 50)

    return BarChartData(data, maxValue, average)
}

fun generateTrendDescription(data: List<BarData>, filter: DateFilter): String {
    if (data.isEmpty() || data.all { it.spend == 0f }) {
        return "No spending data available for this period."
    }

    val total = data.sumOf { it.spend.toDouble() }.toFloat()
    val average = total / data.size
    val recentHalf = data.takeLast(data.size / 2)
    val olderHalf = data.take(data.size / 2)

    val recentAvg = recentHalf.map { it.spend }.average().toFloat()
    val olderAvg = olderHalf.map { it.spend }.average().toFloat()

    if (olderAvg == 0f) {
        return "Keep tracking your expenses to see spending trends!"
    }

    val trend = when {
        recentAvg > olderAvg * 1.15 -> "increasing"
        recentAvg < olderAvg * 0.85 -> "decreasing"
        else -> "stable"
    }

    val period = when (filter) {
        DateFilter.Daily -> "week"
        DateFilter.Weekly -> "month"
        DateFilter.Monthly -> "6 months"
    }

    val highestDay = data.maxByOrNull { it.spend }
    val changePercent = if (olderAvg > 0) {
        ((recentAvg - olderAvg) / olderAvg * 100).toInt()
    } else 100

    return when (trend) {
        "increasing" -> "Spending is trending up by ${changePercent}% this $period. Highest: ${
            highestDay?.name} ($${highestDay?.spend?.toDouble()?.withCommas()})."
        "decreasing" -> "Spending is trending down by ${
            -changePercent}% this $period. You're saving more lately!"
        else -> "Spending is relatively stable this $period at $${
            average.toDouble().withCommas()} average per ${filter.name.lowercase()}."
    }
}

