package com.fwrdgrp.financetracker.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.ui.PieChartData
import com.fwrdgrp.financetracker.ui.composables.home.BalanceCard
import com.fwrdgrp.financetracker.ui.composables.home.BreakdownCard
import com.fwrdgrp.financetracker.ui.composables.home.BudgetCard
import com.fwrdgrp.financetracker.ui.composables.home.ExpenseCard
import com.fwrdgrp.financetracker.ui.navigation.Screen
import com.fwrdgrp.financetracker.ui.uiutils.calculateNextRefreshTimestamp
import com.fwrdgrp.financetracker.ui.uiutils.shouldRollover
import com.fwrdgrp.financetracker.ui.uiutils.toPieChartData
import java.util.Calendar

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val recentTransaction by viewModel.recentTransactions.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val selectedTab by viewModel.dateFilter.collectAsStateWithLifecycle()
    var calendar by remember { mutableStateOf(Calendar.getInstance()) }



    user?.let { user ->
        LaunchedEffect(Unit) {
            user.budget.let {
                if (shouldRollover(it)) {
                    val newTimestamp = calculateNextRefreshTimestamp(it.day ?: 0)
                    viewModel.budgetRollover(newTimestamp)
                }
            }
        }
        Home(
            user,
            selectedTab,
            recentTransaction,
            transactions.toPieChartData(),
            { navController.navigate(Screen.TranDetails(it)) }
        )
        { viewModel.onDateFilterSelect(it, calendar) }
    }
}

@Composable
fun Home(
    user: User,
    selectedTab: DateFilter,
    recentTransactions: List<Transaction>,
    pieChartData: List<PieChartData>,
    navToDetails: (String) -> Unit,
    onSelect: (DateFilter) -> Unit,
) {
    val tabs = DateFilter.entries
    var showBudget by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }
            item { BalanceCard(user.balance) }
            if (user.budget.refresh != null) {
                item { BudgetCard(user.budget, showBudget, { showBudget = it }) }
            }
            item { BreakdownCard(tabs, pieChartData, selectedTab) { onSelect(it) } }
            item { ExpenseCard(recentTransactions, { navToDetails(it) }) }
            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

