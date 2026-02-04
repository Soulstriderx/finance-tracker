package com.fwrdgrp.financetracker.ui.composables.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.data.model.main.Bill
import com.fwrdgrp.financetracker.ui.theme.CreamyTan
import com.fwrdgrp.financetracker.ui.uiutils.getTimeLeft
import com.fwrdgrp.financetracker.ui.uiutils.withCommas

@Composable
fun BillRow(item: Bill, payBill: (Bill) -> Unit, navToDetails: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(8.dp))
            .background(
                color = CreamyTan,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 16.dp)
            .clickable(onClick = { navToDetails(item.uid) }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clickable(onClick = { navToDetails(item.uid) }),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.65f)
            ) {
                Column {
                    Text(
                        item.name.ifBlank { "--" },
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        minLines = 1
                    )
                    Text(
                        "Due in ${getTimeLeft(item.nextDue)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Text(
                modifier = Modifier.weight(0.3f),
                text = "$${item.amount.toDouble().withCommas()}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End
            )
        }
        Spacer(Modifier.height(4.dp))
        Button(onClick = { payBill(item) }, shape = RoundedCornerShape(8.dp)) {
            Text("Pay Bill")
        }
    }
}
