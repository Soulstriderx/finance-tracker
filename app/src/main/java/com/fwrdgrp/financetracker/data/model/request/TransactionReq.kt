package com.fwrdgrp.financetracker.data.model.request

import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.enum.PaymentMethod
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.google.firebase.Timestamp

data class TransactionReq(
    val uid: String = "",
    val newType: TransactionType = TransactionType.Expense,
    val type: TransactionType = TransactionType.Expense,
    val method: PaymentMethod = PaymentMethod.Cash,
    val category: Category = Category.Food,
    val newCategory: Category = Category.Food,
    val customCategory: String? = null,
    val newAmount: String = "",
    val amount: String = "",
    val note: String = "",
    val timestamp: Timestamp? = null,
    val newTimestamp: Timestamp? = null,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val week: Int = 0
) {
    fun toMap(): Map<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf(
            "uid" to uid,
            "type" to type,
            "method" to method,
            "category" to category,
            "amount" to amount,
            "note" to note,
            "year" to year,
            "month" to month,
            "day" to day,
            "week" to week
        )
        timestamp?.let { map["timestamp"] = it }
        customCategory?.let { map["customCategory"] = it }
        return map
    }

    fun toEditMap(): Map<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf(
            "uid" to uid,
            "type" to newType,
            "method" to method,
            "category" to newCategory,
            "amount" to newAmount,
            "note" to note,
            "year" to year,
            "month" to month,
            "day" to day,
            "week" to week
        )
        newTimestamp?.let { map["timestamp"] = it }
        customCategory?.let { map["customCategory"] = it }
        return map
    }
}