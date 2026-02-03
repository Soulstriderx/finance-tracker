package com.fwrdgrp.financetracker.ui.uiutils

import com.fwrdgrp.financetracker.data.datautils.deriveDateFields
import com.fwrdgrp.financetracker.data.enum.BarData
import com.fwrdgrp.financetracker.data.enum.BudgetStatus
import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.fwrdgrp.financetracker.data.model.main.Budget
import com.fwrdgrp.financetracker.data.model.main.MonthlyIncome
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.BudgetReq
import com.fwrdgrp.financetracker.data.model.request.ProfileUpdateReq
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.fwrdgrp.financetracker.data.model.ui.BarChartData
import com.fwrdgrp.financetracker.data.model.ui.PieChartData
import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
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

fun Calendar.toDateRange(endCalInLong: Long): String {
    val endCal = Timestamp(Date(endCalInLong)).toCalendar()
    val sameYear = get(Calendar.YEAR) == endCal.get(Calendar.YEAR)
    val sameMonth = sameYear && get(Calendar.MONTH) == endCal.get(Calendar.MONTH)

    return when {
        sameMonth -> {
            val start = SimpleDateFormat("dd", Locale.getDefault()).format(time)
            val end = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(endCal.time)
            "$start–$end"
        }

        sameYear -> {
            val start = SimpleDateFormat("dd MMM", Locale.getDefault()).format(time)
            val end = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(endCal.time)
            "$start – $end"
        }

        else -> {
            val start = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(time)
            val end = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(endCal.time)
            "$start – $end"
        }
    }
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
            val daysDiff =
                ((referenceCalendar.timeInMillis - transCalendar.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

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

//Function too long. Shorten it
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
            highestDay?.name
        } ($${highestDay?.spend?.toDouble()?.withCommas()})."

        "decreasing" -> "Spending is trending down by ${
            -changePercent
        }% this $period. You're saving more lately!"

        else -> "Spending is relatively stable this $period at $${
            average.toDouble().withCommas()
        } average per ${filter.name.lowercase()}."
    }
}

fun createFormWithTransaction(transaction: Transaction): TransactionReq {
    val calendar = Calendar.getInstance()
    val timestamp = transaction.timestamp ?: Timestamp(calendar.time)

    val derivedDate = deriveDateFields(timestamp.toDate().time)

    return TransactionReq(
        uid = transaction.uid,
        newType = transaction.type,
        type = transaction.type,
        method = transaction.method,
        category = transaction.category,
        newCategory = transaction.category,
        amount = transaction.amount,
        newAmount = transaction.amount,
        note = transaction.note,
        timestamp = timestamp,
        newTimestamp = timestamp,
        year = derivedDate.year,
        month = derivedDate.month,
        day = derivedDate.day,
        week = derivedDate.week
    )
}

fun Transaction.displayCategory(): String {
    return if (category == Category.Other && !customCategory.isNullOrBlank()) customCategory
    else category.name
}

fun calculateAverages(
    transactions: List<Transaction>,
    referenceCalendar: Calendar = Calendar.getInstance()
): Triple<Float, Float, Float> {
    val dailyData = aggregateDailyTransactions(transactions, referenceCalendar)
    val dailyAverage = if (dailyData.isNotEmpty()) {
        dailyData.map { it.spend }.sum() / dailyData.size
    } else 0f

    val weeklyData = aggregateWeeklyTransactions(transactions, referenceCalendar)
    val weeklyAverage = if (weeklyData.isNotEmpty()) {
        weeklyData.map { it.spend }.sum() / weeklyData.size
    } else 0f

    val monthlyData = aggregateMonthlyTransactions(transactions, referenceCalendar)
    val monthlyAverage = if (monthlyData.isNotEmpty()) {
        monthlyData.map { it.spend }.sum() / monthlyData.size
    } else 0f

    return Triple(monthlyAverage, weeklyAverage, dailyAverage)
}

fun calculateTotalBudget(budget: Budget): String {
    val budgetList = listOf(
        budget.food?.toDouble() ?: 0.0,
        budget.transportation?.toDouble() ?: 0.0,
        budget.entertainment?.toDouble() ?: 0.0,
        budget.household?.toDouble() ?: 0.0
    )
    return budgetList.sum().toString()
}

fun getTimeLeft(timestamp: Timestamp?): String {
    if (timestamp == null) return "No date available"

    val diff = timestamp.toDate().time - System.currentTimeMillis()

    if (diff <= 0) return "Date expired"

    val days = TimeUnit.MILLISECONDS.toDays(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24

    return "$days days, $hours hours"
}

fun createProfileForm(user: User): ProfileUpdateReq {
    return ProfileUpdateReq(
        balance = user.balance,
        budget = BudgetReq.fromMap(user.budget.toMap()),
        monthlyIncome = MonthlyIncome.fromMap(user.monthlyIncome.toMap())
    )
}

fun calcOverbudget(used: String, base: String): Boolean {
    return used.toDouble() > base.toDouble()
}

fun shouldRollover(budget: Budget): Boolean {
    val currentRefreshCalendar = budget.refresh?.toCalendar() ?: return false
    val today = Calendar.getInstance()
    val hasRefreshDatePassed = currentRefreshCalendar.timeInMillis <= today.timeInMillis

    return hasRefreshDatePassed
}

fun calculateNextRefreshTimestamp(originalRefreshDay: Int): Timestamp {
    val nextRefreshCalendar = Calendar.getInstance().apply {
        add(Calendar.MONTH, 1)

        val maxDayInNextMonth = getActualMaximum(Calendar.DAY_OF_MONTH)
        val targetDay = minOf(originalRefreshDay, maxDayInNextMonth)

        set(Calendar.DAY_OF_MONTH, targetDay)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return nextRefreshCalendar.toTimestamp()
}

fun getLowBudgetWarning(budget: Budget): String? {
    fun getStatus(used: String?, total: String?): BudgetStatus? {
        val usedVal = used?.toDoubleOrNull() ?: return null
        val totalVal = total?.toDoubleOrNull() ?: return null

        if (totalVal == 0.0) return null

        val percentUsed = (usedVal / totalVal) * 100

        return when {
            percentUsed >= 100 -> BudgetStatus.EXCEEDED
            percentUsed >= 90 -> BudgetStatus.EXCEEDING
            percentUsed >= 80 -> BudgetStatus.NEARING
            else -> null
        }
    }

    val nearing = mutableListOf<String>()
    val exceeding = mutableListOf<String>()
    val exceeded = mutableListOf<String>()

    fun check(name: String, used: String?, total: String?) {
        when (getStatus(used, total)) {
            BudgetStatus.NEARING -> nearing.add(name)
            BudgetStatus.EXCEEDING -> exceeding.add(name)
            BudgetStatus.EXCEEDED -> exceeded.add(name)
            null -> {}
        }
    }

    check("Food", budget.foodUsed, budget.food)
    check("Transportation", budget.transportationUsed, budget.transportation)
    check("Entertainment", budget.entertainmentUsed, budget.entertainment)
    check("Household", budget.householdUsed, budget.household)

    fun format(list: List<String>): String =
        when (list.size) {
            0 -> ""
            1 -> list[0]
            2 -> "${list[0]} and ${list[1]}"
            else -> "${list.dropLast(1).joinToString(", ")} and ${list.last()}"
        }

    val parts = mutableListOf<String>()

    if (exceeded.isNotEmpty()) {
        parts.add("${format(exceeded)} exceeded the budget")
    }

    if (exceeding.isNotEmpty()) {
        parts.add("${format(exceeding)} is exceeding the budget")
    }

    if (nearing.isNotEmpty()) {
        parts.add("${format(nearing)} is nearing the budget limit")
    }

    return if (parts.isEmpty()) null
    else parts.joinToString(". ")
}

fun toRange(filter: DateFilter): Pair<Long, Long> {
    val endCal = Calendar.getInstance()
    val startCal = endCal.clone() as Calendar

    when (filter) {
        DateFilter.Daily -> {
            startCal.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        DateFilter.Weekly -> {
            startCal.add(Calendar.DAY_OF_YEAR, -7)
        }

        DateFilter.Monthly -> {
            startCal.add(Calendar.DAY_OF_YEAR, -30)
        }
    }

    return startCal.timeInMillis to endCal.timeInMillis
}


