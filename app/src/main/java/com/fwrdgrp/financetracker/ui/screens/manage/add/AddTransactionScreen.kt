package com.fwrdgrp.financetracker.ui.screens.manage.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.ui.composables.manage.ManageExpense
import com.fwrdgrp.financetracker.ui.uiutils.createFormWithToday

@Composable
fun AddTransactionScreen(
    navController: NavController,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.popBackStack()
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    ManageExpense(
        createFormWithToday(),
        showDialog,
        { showDialog = it },
        onCancel = { navController.popBackStack() },
    ) { viewModel.addTransaction(it) }
}