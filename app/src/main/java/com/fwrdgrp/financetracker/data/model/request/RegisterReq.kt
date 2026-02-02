package com.fwrdgrp.financetracker.data.model.request

import com.fwrdgrp.financetracker.data.model.main.Budget
import com.fwrdgrp.financetracker.data.model.main.MonthlyIncome

data class RegisterReq(
    val uid: String? = null,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val password2: String = "",
    val monthlyIncome: MonthlyIncome = MonthlyIncome(),
    val budget: Budget = Budget(),
    val balance: String? = null
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf(
            "name" to name,
            "monthlyIncome" to monthlyIncome,
            "budget" to budget,
            "email" to email,
        )
        uid?.let { map["uid"] = it }
        balance?.let { map["balance"] = it }
        return map
    }
}
