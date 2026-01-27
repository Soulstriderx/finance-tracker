package com.fwrdgrp.financetracker.ui.screens.manage.add

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
    val customCategories by viewModel.customCategories.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var showCustomDialog by remember { mutableStateOf(false) }

    ManageExpense(
        createFormWithToday(),
        showDialog,
        { showDialog = it },
        showCustomDialog,
        {showCustomDialog = it},
        {viewModel.addCustomCategory(it)},
        {viewModel.deleteCustomCategory(it)},
        customCategories = customCategories,
        onCancel = { navController.popBackStack() },
    ) { viewModel.addTransaction(it) }
}