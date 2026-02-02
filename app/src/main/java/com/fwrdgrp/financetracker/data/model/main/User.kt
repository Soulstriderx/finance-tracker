package com.fwrdgrp.financetracker.data.model.main

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val monthlyIncome: MonthlyIncome = MonthlyIncome(),
    val budget: Budget = Budget(),
    val lifetimeSpend: String = "0",
    val lifetimeIncome: String = "0",
    val balance: String = ""
) {
    companion object {
        fun fromMap(map: Map<String, Any>): User {
            return User(
                uid = map["uid"] as? String ?: "",
                name = map["name"] as? String ?: "",
                email = map["email"] as? String ?: "",
                monthlyIncome = (map["monthlyIncome"] as? Map<String, Any>)?.let {
                    MonthlyIncome.fromMap(it)
                } ?: MonthlyIncome(),
                budget = (map["budget"] as? Map<String, String>)?.let {
                    Budget.fromMap(it)
                } ?: Budget(),
                lifetimeSpend = map["lifetimeSpend"] as? String ?: "0",
                lifetimeIncome = map["lifetimeIncome"] as? String ?: "0",
                balance = map["balance"] as? String ?: ""
            )
        }
    }
}
