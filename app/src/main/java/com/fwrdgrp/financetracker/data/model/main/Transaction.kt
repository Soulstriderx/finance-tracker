package com.fwrdgrp.financetracker.data.model.main

import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.google.firebase.Timestamp

data class Transaction(
    val type: TransactionType = TransactionType.EXPENSE,
    val category: String = "",
    val amount: Double = 0.0,
    val timestamp: Timestamp? = null,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val week: Int = 0
)
