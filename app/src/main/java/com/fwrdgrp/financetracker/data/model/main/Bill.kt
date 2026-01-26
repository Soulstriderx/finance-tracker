package com.fwrdgrp.financetracker.data.model.main

import com.fwrdgrp.financetracker.data.enum.BillFrequency
import com.google.firebase.Timestamp

data class Bill(
    val amount: String = "",
    val category: String = "",
    val frequency: BillFrequency = BillFrequency.Monthly,
    val nextDue: Timestamp? = null
)
