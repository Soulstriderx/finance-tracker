package com.fwrdgrp.financetracker.ui.composables.home

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.ui.composables.general.RoundedColumn

@Composable
fun ExpenseCard(transaction: List<Transaction>, navToDetails: (String) -> Unit) {
    if (transaction.isNotEmpty()) {
        RoundedColumn {
            Text(
                text = "Recent Expenses",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            HorizontalDivider(thickness = 1.dp)

            transaction.forEachIndexed { index, item ->
                ExpenseRow(item) { navToDetails(it) }
                if (index < transaction.size - 1) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color(0, 0, 0, 20)
                    )
                }
            }
        }
    }
}
