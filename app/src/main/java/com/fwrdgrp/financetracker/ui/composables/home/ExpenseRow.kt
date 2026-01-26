package com.fwrdgrp.financetracker.ui.composables.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Interests
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.ui.uiutils.toDisplayName
import com.fwrdgrp.financetracker.ui.uiutils.withCommas

@Composable
fun ExpenseRow(item: Transaction) {
    val categoryIcon = mapOf(
        "FOOD" to Pair(Icons.Filled.Fastfood, Color(0xFFFF6B6B)),
        "TRANSPORTATION" to Pair(Icons.Filled.LocalTaxi, Color(0xFF4ECDC4)),
        "ENTERTAINMENT" to Pair(Icons.Filled.SportsEsports, Color(0xFFFFBE0B)),
        "HOUSEHOLD" to Pair(Icons.Filled.House, Color(0xFF95E1D3)),
        "OTHER" to Pair(Icons.Filled.Interests, Color(0xFFB4A7D6)),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                categoryIcon[item.category]?.first ?: Icons.Filled.Category,
                "",
                Modifier.size(30.dp),
                tint = categoryIcon[item.category]?.second ?: Color.Black
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    item.note,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    item.category.toDisplayName(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Text(
            text = "$${item.amount.toDouble().withCommas()}",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}