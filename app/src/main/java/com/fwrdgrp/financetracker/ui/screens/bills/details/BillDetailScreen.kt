package com.fwrdgrp.financetracker.ui.screens.bills.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fwrdgrp.financetracker.ui.composables.manage.ManageBillDialog
import com.fwrdgrp.financetracker.ui.uiutils.createBillReqWithBill
import com.fwrdgrp.financetracker.ui.uiutils.getTimeLeft

@Composable
fun BillDetailScreen(
    viewModel: BillDetailViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    val bill by viewModel.bill.collectAsStateWithLifecycle()

    bill?.let { bill ->
        if (showDialog) {
            ManageBillDialog(
                title = "Edit Bill",
                isEdit = true,
                form = createBillReqWithBill(bill),
                showDateDialog = showDateDialog,
                onDateDialogChange = { showDateDialog = it },
                onDismiss = { showDialog = false }) { viewModel.editBill(it) }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            bill.name,
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Amount : $${bill.amount}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Due in ${getTimeLeft(bill.nextDue)}.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
                item {
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(Modifier.height(12.dp))
                }
                item {
                    if (bill.paymentHistory.isNotEmpty()) {
                        Text(
                            "Paid Bills",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    if (bill.paymentHistory.isNotEmpty()) {
                        bill.paymentHistory.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.monthYear, Modifier.weight(1f))
                                IconButton(onClick = { viewModel.deletePayment(bill, item.uid) }) {
                                    Icon(Icons.Default.Delete, null)
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Pay a bill to see the history!",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit")
                }
                Button(
                    onClick = { viewModel.deleteBill(bill.uid) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}