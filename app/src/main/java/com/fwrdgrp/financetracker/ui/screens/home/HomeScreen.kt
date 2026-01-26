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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.ui.PieChartData
import com.fwrdgrp.financetracker.ui.composables.home.BalanceCard
import com.fwrdgrp.financetracker.ui.composables.home.BreakdownCard
import com.fwrdgrp.financetracker.ui.composables.home.ExpenseCard
import com.fwrdgrp.financetracker.ui.uiutils.dummyTransactions

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val transactions = dummyTransactions

    user?.let { user ->
        Home(user, transactions)
    }
}

@Composable
fun Home(user: User, transactions: List<Transaction>) {

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Daily", "Weekly", "Monthly")

    val sampleData = listOf(
        PieChartData("Category A", 30f, Color(0xFF6200EE)),
        PieChartData("Category B", 25f, Color(0xFF03DAC5)),
        PieChartData("Category C", 20f, Color(0xFFFF6F00)),
        PieChartData("Category D", 15f, Color(0xFF00C853)),
        PieChartData("Category E", 10f, Color(0xFFD50000))
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }
            item { BalanceCard(user.balance) }
            item { BreakdownCard(tabs, sampleData, selectedTab) { selectedTab = it } }
            item { ExpenseCard(transactions) }
            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

