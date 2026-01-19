package com.fwrdgrp.financetracker.data.model.request

import com.fwrdgrp.financetracker.data.model.main.MonthlyIncome

data class RegisterReq(
    val uid: String? = null,
    val name: String,
    val email: String,
    val password: String,
    val monthlyIncome: MonthlyIncome? = null,
    val balance: Double? = null
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "name" to name,
            "email" to email,
        )
        uid?.let { map["uid"] = it }
        monthlyIncome?.let { map["monthlyIncome"] = MonthlyIncome(it.amount, it.payday) }
        balance?.let { map["balance"] = it }
        return map
    }
}
