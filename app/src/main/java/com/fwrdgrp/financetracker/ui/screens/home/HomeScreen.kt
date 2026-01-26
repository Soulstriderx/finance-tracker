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
import com.fwrdgrp.financetracker.ui.composables.home.ExpenseCard
import com.fwrdgrp.financetracker.ui.uiutils.toPieChartData

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()


    user?.let { user ->
        Home(user, transactions, transactions.toPieChartData())
    }
}

@Composable
fun Home(
    user: User,
    transactions: List<Transaction>,
    pieChartData: List<PieChartData>
) {
    val tabs = DateFilter.entries
    var selectedTab by remember { mutableStateOf(tabs.first()) }


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }
            item { BalanceCard(user.balance) }
            item { BreakdownCard(tabs, pieChartData, selectedTab) { selectedTab = it } }
            item { ExpenseCard(transactions) }
            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

