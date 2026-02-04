package com.fwrdgrp.financetracker.ui.screens.manage.edit

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.ui.composables.manage.ManageExpense
import com.fwrdgrp.financetracker.ui.uiutils.createFormWithTransaction
import kotlinx.coroutines.launch

@Composable
fun EditTransactionScreen(
    navController: NavController,
    viewModel: EditTransactionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        launch {
            viewModel.toast.collect { msg ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
        launch {
            viewModel.finish.collect {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("transaction_updated", true)
                navController.popBackStack()
            }
        }
    }

    val transaction by viewModel.transaction.collectAsStateWithLifecycle()

    val customCategories by viewModel.customCategories.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var showCustomDialog by remember { mutableStateOf(false) }

    transaction?.let { transaction ->
        ManageExpense(
            createFormWithTransaction(transaction),
            showDialog,
            { showDialog = it },
            showCustomDialog,
            { showCustomDialog = it },
            {
                if (viewModel.validateCustomCat(it)) {
                    viewModel.addCustomCategory(it)
                    showCustomDialog = false
                }
            },
            { viewModel.deleteCustomCategory(it) },
            true,
            customCategories = customCategories,
            onCancel = { navController.popBackStack() },
        ) { viewModel.updateTransaction(it) }
    }
}