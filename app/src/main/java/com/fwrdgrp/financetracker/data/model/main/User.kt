package com.fwrdgrp.financetracker.data.model.main

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val monthlyIncome: MonthlyIncome = MonthlyIncome(),
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
                balance = map["balance"] as? String ?: ""
            )
        }
    }
}
