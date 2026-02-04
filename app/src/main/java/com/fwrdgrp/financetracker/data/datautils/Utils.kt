package com.fwrdgrp.financetracker.data.datautils

import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.enum.PaymentMethod
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.fwrdgrp.financetracker.data.model.main.Bill
import com.fwrdgrp.financetracker.data.model.main.Budget
import com.fwrdgrp.financetracker.data.model.main.DerivedDate
import com.fwrdgrp.financetracker.data.model.main.Payment
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.fwrdgrp.financetracker.ui.uiutils.toCalendar
import com.fwrdgrp.financetracker.ui.uiutils.toTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun deriveDateFields(timestamp: Long): DerivedDate {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp

    return DerivedDate(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH),
        week = calendar.get(Calendar.WEEK_OF_YEAR)
    )
}

fun calculateDateRange(
    filter: DateFilter,
    referenceCalendar: Calendar = Calendar.getInstance()
): Pair<Long, Long> {
    val end = referenceCalendar.timeInMillis

    val start = referenceCalendar.clone() as Calendar
    when (filter) {
        DateFilter.Daily -> start.add(Calendar.DAY_OF_MONTH, -1)
        DateFilter.Weekly -> start.add(Calendar.DAY_OF_MONTH, -7)
        DateFilter.Monthly -> start.add(Calendar.DAY_OF_MONTH, -30)
    }

    return start.timeInMillis to end
}

fun calculateStatsDateRange(
    filter: DateFilter,
    referenceCalendar: Calendar = Calendar.getInstance()
): Pair<Long, Long> {
    val end = referenceCalendar.timeInMillis

    val start = (referenceCalendar.clone() as Calendar).apply {
        when (filter) {
            DateFilter.Daily -> add(Calendar.DAY_OF_MONTH, -6)
            DateFilter.Weekly -> add(Calendar.WEEK_OF_YEAR, -3)
            DateFilter.Monthly -> add(Calendar.MONTH, -5)
        }
    }

    return start.timeInMillis to end
}

private fun isWithinCurrentBudgetPeriod(
    transactionTimestamp: Timestamp,
    refreshDate: Int
): Boolean {
    val transactionCalendar = transactionTimestamp.toCalendar()
    val now = Calendar.getInstance()

    val currentDay = now.get(Calendar.DAY_OF_MONTH)

    // To calculate start of period (will be Inclusive)
    val budgetPeriodStart = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, refreshDate)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        if (currentDay < refreshDate) {
            add(Calendar.MONTH, -1)
        }
    }

    // Get the next start period (will be Exclusive)
    val budgetPeriodEnd = (budgetPeriodStart.clone() as Calendar).apply {
        add(Calendar.MONTH, 1)
    }

    return transactionCalendar.timeInMillis >= budgetPeriodStart.timeInMillis &&
            transactionCalendar.timeInMillis < budgetPeriodEnd.timeInMillis
}

private fun getCurrentBudgetValue(fieldName: String, budget: Budget): Double {
    return when (fieldName) {
        "budget.foodUsed" -> budget.foodUsed?.toDouble() ?: 0.0
        "budget.transportationUsed" -> budget.transportationUsed?.toDouble() ?: 0.0
        "budget.entertainmentUsed" -> budget.entertainmentUsed?.toDouble() ?: 0.0
        "budget.householdUsed" -> budget.householdUsed?.toDouble() ?: 0.0
        else -> 0.0
    }
}

fun TransactionType.calculateMetricChanges(
    amount: Double,
    isAdding: Boolean = true
): Triple<Double, Double, Double> {
    val multiplier = if (isAdding) 1.0 else -1.0

    val balanceChange = when (this) {
        TransactionType.Income -> amount * multiplier
        TransactionType.Expense -> -amount * multiplier
    }

    val lifetimeSpendChange = if (this == TransactionType.Expense) {
        amount * multiplier
    } else 0.0

    val lifetimeIncomeChange = if (this == TransactionType.Income) {
        amount * multiplier
    } else 0.0

    return Triple(balanceChange, lifetimeSpendChange, lifetimeIncomeChange)
}

operator fun Triple<Double, Double, Double>.plus(
    other: Triple<Double, Double, Double>
): Triple<Double, Double, Double> {
    return Triple(
        this.first + other.first,
        this.second + other.second,
        this.third + other.third
    )
}

fun Category.calculateBudgetUpdates(
    amount: Double,
    type: TransactionType,
    timestamp: Timestamp,
    budget: Budget,
    isAdding: Boolean
): Map<String, Any> {
    if (type == TransactionType.Income) return emptyMap()


    if (!isWithinCurrentBudgetPeriod(timestamp, budget.day ?: 0)) {
        return emptyMap()
    }

    // Check if the budget for this category is set (not null)
    val isBudgetSet = when (this) {
        Category.Food -> budget.food != null
        Category.Transportation -> budget.transportation != null
        Category.Entertainment -> budget.entertainment != null
        Category.Household -> budget.household != null
        else -> false
    }

    // If budget is not set for this category, ignore
    if (!isBudgetSet) {
        return emptyMap()
    }

    val fieldName = when (this) {
        Category.Food -> "budget.foodUsed"
        Category.Transportation -> "budget.transportationUsed"
        Category.Entertainment -> "budget.entertainmentUsed"
        Category.Household -> "budget.householdUsed"
        else -> return emptyMap()
    }

    val currentUsed = when (this) {
        Category.Food -> budget.foodUsed?.toDouble() ?: 0.0
        Category.Transportation -> budget.transportationUsed?.toDouble() ?: 0.0
        Category.Entertainment -> budget.entertainmentUsed?.toDouble() ?: 0.0
        Category.Household -> budget.householdUsed?.toDouble() ?: 0.0
        else -> 0.0
    }

    val newUsed = if (isAdding) {
        currentUsed + amount
    } else {
        (currentUsed - amount).coerceAtLeast(0.0)
    }

    return mapOf(fieldName to newUsed.toString())
}

fun Category.calculateBudgetUpdatesForEdit(
    oldAmount: Double,
    oldType: TransactionType,
    oldTimestamp: Timestamp,
    newCategory: Category,
    newAmount: Double,
    newType: TransactionType,
    newTimestamp: Timestamp,
    budget: Budget
): Map<String, Any> {
    val result = mutableMapOf<String, Any>()

    // Add from oldCategory if its within period
    val oldUpdates = this.calculateBudgetUpdates(
        amount = oldAmount,
        type = oldType,
        timestamp = oldTimestamp,
        budget = budget,
        isAdding = false
    )

    // Add from newCategory if within period
    val newUpdates = newCategory.calculateBudgetUpdates(
        amount = newAmount,
        type = newType,
        timestamp = newTimestamp,
        budget = budget,
        isAdding = true
    )

    // If old transaction was in current period, remove.
    result.putAll(oldUpdates)

    if (this == newCategory && oldUpdates.isNotEmpty() && newUpdates.isNotEmpty()) {
        newUpdates.forEach { (fieldName, newValue) ->
            val currentBudgetValue = getCurrentBudgetValue(fieldName, budget)
            val oldChange = (oldUpdates[fieldName] as? String)?.toDoubleOrNull()?.let {
                it - currentBudgetValue
            } ?: 0.0
            val newChange = (newValue as? String)?.toDoubleOrNull()?.let {
                it - currentBudgetValue
            } ?: 0.0

            val finalValue = (currentBudgetValue + oldChange + newChange).coerceAtLeast(0.0)
            result[fieldName] = finalValue.toString()
        }
    } else {
        result.putAll(newUpdates)
    }
    return result
}

suspend fun DocumentReference.updateUserMetricsAndBudget(
    metricChanges: Triple<Double, Double, Double>,
    budgetUpdates: Map<String, Any>,
    currentBalance: Double,
    currentLifetimeSpend: Double,
    currentLifetimeIncome: Double
) {
    val (balanceChange, lifetimeSpendChange, lifetimeIncomeChange) = metricChanges

    val updateMap = mutableMapOf<String, Any>(
        "balance" to (currentBalance + balanceChange).toString(),
        "lifetimeSpend" to (currentLifetimeSpend + lifetimeSpendChange).toString(),
        "lifetimeIncome" to (currentLifetimeIncome + lifetimeIncomeChange).toString()
    )

    updateMap.putAll(budgetUpdates)

    this.update(updateMap).await()
}

fun createIncomeTransaction(user: User): TransactionReq {
    val calendar = Calendar.getInstance()
    val derivedDate = deriveDateFields(calendar.timeInMillis)
    return TransactionReq(
        type = TransactionType.Income,
        method = PaymentMethod.FPX,
        category = Category.Other,
        customCategory = "Salary",
        amount = user.monthlyIncome.amount,
        note = "Monthly Income",
        timestamp = calendar.toTimestamp(),
        year = derivedDate.year,
        week = derivedDate.week,
        month = derivedDate.month,
        day = derivedDate.day
    )
}

fun createBillTransaction(bill: Bill, uid: String): TransactionReq {
    val calendar = Calendar.getInstance()
    val derivedDate = deriveDateFields(calendar.timeInMillis)
    return TransactionReq(
        uid = uid,
        type = TransactionType.Expense,
        method = PaymentMethod.FPX,
        category = Category.Other,
        customCategory = "Bill",
        amount = bill.amount,
        note = bill.name,
        timestamp = calendar.toTimestamp(),
        year = derivedDate.year,
        week = derivedDate.week,
        month = derivedDate.month,
        day = derivedDate.day
    )
}

fun createPaymentRecord(uid: String, bill: Bill): Payment {
    val currentDate = bill.nextDue ?: Timestamp.now()
    val calendar = Calendar.getInstance().apply {
        time = currentDate.toDate()
    }
    val monthYear = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)

    return Payment(
        uid = uid,
        paidDate = currentDate,
        monthYear = monthYear
    )
}

