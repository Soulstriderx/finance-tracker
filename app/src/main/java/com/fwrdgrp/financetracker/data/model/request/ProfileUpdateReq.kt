package com.fwrdgrp.financetracker.data.model.request

import com.fwrdgrp.financetracker.data.model.main.MonthlyIncome

data class ProfileUpdateReq(
    val balance: String? = null,
    val budget: BudgetReq = BudgetReq(),
    val monthlyIncome: MonthlyIncome = MonthlyIncome()
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        balance?.let { map["balance"] = it }
        budget.food?.let { map["budget.food"] = it }
        budget.transportation?.let { map["budget.transportation"] = it }
        budget.entertainment?.let { map["budget.entertainment"] = it }
        budget.household?.let { map["budget.household"] = it }
        budget.refresh?.let { map["budget.refresh"] = it }
        budget.day?.let { map["budget.day"] = it }
        monthlyIncome.amount.let { map["monthlyIncome.amount"] = it }
        monthlyIncome.day?.let { map["monthlyIncome.day"] = it }
        monthlyIncome.payday?.let { map["monthlyIncome.payday"] = it }
        return map
    }
}
