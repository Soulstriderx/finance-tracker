package com.fwrdgrp.financetracker.data.model.main

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val monthlyIncome: MonthlyIncome? = null,
    val balance: Double? = null
)
