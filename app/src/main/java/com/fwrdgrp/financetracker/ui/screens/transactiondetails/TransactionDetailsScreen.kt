package com.fwrdgrp.financetracker.ui.screens.transactiondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.ui.composables.general.Chip
import com.fwrdgrp.financetracker.ui.navigation.Screen
import com.fwrdgrp.financetracker.ui.uiutils.withCommas
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionDetailsScreen(
    navController: NavController,
    viewModel: TransactionDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow(
            "transaction_updated",
            false
        )?.collect { updated ->
            if (updated) {
                viewModel.fetchTransactionById(viewModel.uid)
                navController.currentBackStackEntry?.savedStateHandle
                    ?.set("transaction_updated", false)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.popBackStack()
        }
    }

    val transaction by viewModel.transaction.collectAsStateWithLifecycle()

    transaction?.let { transaction ->
        TransactionDetails(transaction, viewModel::deleteTransaction)
        { navController.navigate(Screen.Edit(transaction.uid)) }
    }
}

@Composable
fun TransactionDetails(transaction: Transaction, onDelete: () -> Unit, onEdit: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(0.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(8.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    "$${transaction.amount.toDouble().withCommas()}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Chip(transaction.category.name, transaction.category.icon)
                    Spacer(Modifier.width(8.dp))
                    Chip(transaction.type.name)
                }
                HorizontalDivider(thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.CalendarMonth, null)
                    Spacer(Modifier.width(4.dp))
                    Text(transaction.timestamp?.toDate()?.let { dateFormat.format(it) } ?: "-")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(8.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Note",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                HorizontalDivider(thickness = 1.dp)
                Text(
                    transaction.note.ifBlank { "--" },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Spacer(Modifier.height(100.dp))
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            HorizontalDivider(thickness = 1.dp)
            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Button(
                    onClick = { onEdit() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Edit",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { onDelete() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Delete",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Spacer(Modifier.height(0.dp))
        }
    }
}
