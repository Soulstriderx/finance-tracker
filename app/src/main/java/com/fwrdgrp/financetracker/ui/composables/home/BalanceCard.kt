package com.fwrdgrp.financetracker.ui.composables.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fwrdgrp.financetracker.ui.composables.general.RoundedColumn
import com.fwrdgrp.financetracker.ui.uiutils.withCommas

@Composable
fun BalanceCard(balance: String) {
    RoundedColumn {
        Text("Current Balance")
        Text(
            text = "$${balance.toDouble().withCommas()}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}