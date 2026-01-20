package com.fwrdgrp.financetracker.data.datautils

import com.fwrdgrp.financetracker.data.model.main.DerivedDate
import java.util.Calendar

fun deriveDateFields(timestamp: Long): DerivedDate {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp

    return DerivedDate(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH),
        week = calendar.get(Calendar.WEEK_OF_MONTH)
    )
}
