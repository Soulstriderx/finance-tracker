package com.fwrdgrp.financetracker.data.datautils

import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.main.DerivedDate
import java.util.Calendar

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

