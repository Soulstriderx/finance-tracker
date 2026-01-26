package com.fwrdgrp.financetracker.data.model.main

import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.enum.PaymentMethod
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.google.firebase.Timestamp

data class Transaction(
    val uid: String = "",
    val type: TransactionType = TransactionType.Expense,
    val method: PaymentMethod = PaymentMethod.Cash,
    val category: Category = Category.Food,
    val amount: String = "",
    val note: String = "",
    val timestamp: Timestamp? = null,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val week: Int = 0
)
