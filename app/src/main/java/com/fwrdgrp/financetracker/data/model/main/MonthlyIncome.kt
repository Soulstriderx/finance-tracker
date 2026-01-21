package com.fwrdgrp.financetracker.data.model.main

import com.google.firebase.Timestamp

data class MonthlyIncome(
    val amount: String = "",
    val day: Int? = null,
    val payday: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mutableMapOf<String, Any>(
            "amount" to amount,
            "day" to (day ?: 0),
            "payday" to (payday ?: 0)
        )
    }
}
