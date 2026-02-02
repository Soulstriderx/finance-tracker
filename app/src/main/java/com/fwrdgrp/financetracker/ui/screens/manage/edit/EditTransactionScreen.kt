package com.fwrdgrp.financetracker.ui.screens.manage.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.ui.composables.manage.ManageExpense
import com.fwrdgrp.financetracker.ui.uiutils.createFormWithTransaction

@Composable
fun EditTransactionScreen(
    navController: NavController,
    viewModel: EditTransactionViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("transaction_updated", true)
            navController.popBackStack()
        }
    }

    val transaction by viewModel.transaction.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }

    transaction?.let { transaction ->
        ManageExpense(
            createFormWithTransaction(transaction),
            showDialog,
            { showDialog = it },
            true,
            onCancel = { navController.popBackStack() },
        ) { viewModel.updateTransaction(it) }
    }
}