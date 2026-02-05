package com.fwrdgrp.financetracker.data.model.main

import com.google.firebase.Timestamp

data class MonthlyIncome(
    val amount: String = "",
    val day: Int? = null,
    val payday: Timestamp? = null
) {
    companion object {
        fun fromMap(map: Map<String, Any>): MonthlyIncome {
            return MonthlyIncome(
                amount = map["amount"] as? String ?: "",
                day = (map["day"] as? Number)?.toInt() ?: 0,
                payday = map["payday"] as? Timestamp
            )
        }
    }

    fun toMap(): Map<String, Any> {
        return mutableMapOf<String, Any>(
            "amount" to amount,
            "day" to (day ?: 0),
            "payday" to (payday ?: 0)
        )
    }
}
