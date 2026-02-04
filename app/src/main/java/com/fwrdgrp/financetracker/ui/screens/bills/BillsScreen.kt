package com.fwrdgrp.financetracker.ui.screens.bills

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.data.model.request.BillReq
import com.fwrdgrp.financetracker.ui.composables.manage.BillRow
import com.fwrdgrp.financetracker.ui.composables.manage.ManageBillDialog
import com.fwrdgrp.financetracker.ui.navigation.Screen
import com.fwrdgrp.financetracker.ui.uiutils.calculateNextBillDue
import com.google.firebase.Timestamp

@Composable
fun BillsScreen(
    navController: NavController,
    viewModel: BillsViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    val bills by viewModel.bills.collectAsStateWithLifecycle()

    if (showDialog) {
        ManageBillDialog(
            form = BillReq(),
            showDateDialog = showDateDialog,
            onDateDialogChange = { showDateDialog = it },
            onDismiss = { showDialog = false }) {
            viewModel.addBill(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 125.dp, start = 24.dp, end = 24.dp, top = 24.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(bills) { bill ->
                BillRow(
                    bill,
                    {
                        val nextTimestamp =
                            calculateNextBillDue(
                                bill.day,
                                bill.nextDue ?: Timestamp.now()
                            )
                        viewModel.payBill(it, nextTimestamp)
                    })
                { navController.navigate(Screen.BillDetails(it)) }
            }
        }
        Button(
            onClick = { showDialog = true },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("Add a Bill")
        }
    }
}