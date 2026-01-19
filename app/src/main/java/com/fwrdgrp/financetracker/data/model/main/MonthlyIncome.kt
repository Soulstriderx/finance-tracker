package com.fwrdgrp.financetracker.data.model.main

import com.google.firebase.Timestamp

data class MonthlyIncome(
    val amount: Double = 0.0,
    val payday: Timestamp? = null
)
