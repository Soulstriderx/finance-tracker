package com.fwrdgrp.financetracker.data.model.main

import java.util.Calendar

data class MonthlyIncome(
    val amount: String = "",
    val day: Int? = null,
    val payday: Calendar? = null
) {
    fun toMap(): Map<String, Any> {
        return mutableMapOf<String, Any>(
            "amount" to amount,
            "day" to (day ?: 0),
        )
    }
}
