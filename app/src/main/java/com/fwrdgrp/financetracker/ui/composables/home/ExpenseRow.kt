package com.fwrdgrp.financetracker.ui.composables.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.ui.uiutils.displayCategory
import com.fwrdgrp.financetracker.ui.uiutils.withCommas

@Composable
fun ExpenseRow(item: Transaction, navToDetails: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = { navToDetails(item.uid) }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.65f)
        ) {
            Icon(
                item.category.icon,
                "",
                Modifier.size(30.dp),
                tint = item.category.color
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    item.note.ifBlank { "--" },
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    minLines = 1
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        item.displayCategory(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        item.type.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Text(
            modifier = Modifier.weight(0.3f),
            text = "$${item.amount.toDouble().withCommas()}",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.End
        )
    }
}