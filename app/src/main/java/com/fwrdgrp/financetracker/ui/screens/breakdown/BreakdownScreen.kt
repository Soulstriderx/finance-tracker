package com.fwrdgrp.financetracker.ui.screens.breakdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.fwrdgrp.financetracker.ui.composables.general.LargeTitleText
import com.fwrdgrp.financetracker.ui.composables.general.RoundedColumn
import com.fwrdgrp.financetracker.ui.composables.general.TextValueRow
import com.fwrdgrp.financetracker.ui.uiutils.withCommas

@Composable
fun BreakdownScreen(
    viewModel: BreakdownViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val dateRange by viewModel.dateRange.collectAsStateWithLifecycle()
    val sumIncome =
        transactions.sumOf { if (it.type == TransactionType.Income) it.amount.toDouble() else 0.0 }
    val sumExpense =
        transactions.sumOf { if (it.type == TransactionType.Expense) it.amount.toDouble() else 0.0 }
    val foodSum = transactions.sumOf {
        if (it.category == Category.Food) it.amount.toDoubleOrNull() ?: 0.0 else 0.0
    }
    val transportationSum = transactions.sumOf {
        if (it.category == Category.Transportation) it.amount.toDoubleOrNull() ?: 0.0 else 0.0
    }
    val entertainmentSum = transactions.sumOf {
        if (it.category == Category.Entertainment) it.amount.toDoubleOrNull() ?: 0.0 else 0.0
    }
    val householdSum = transactions.sumOf {
        if (it.category == Category.Household) it.amount.toDoubleOrNull() ?: 0.0 else 0.0
    }
    val otherSum = transactions.sumOf {
        if (it.category == Category.Other && it.type == TransactionType.Expense) {
            it.amount.toDoubleOrNull()
                ?: 0.0
        } else 0.0
    }

    var showOther by remember { mutableStateOf(false) }
    val otherGrouped: Map<String, Double> =
        transactions
            .filter { it.category == Category.Other && it.type == TransactionType.Expense }
            .groupBy { it.customCategory ?: "Other" }
            .mapValues { (_, items) ->
                items.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            }
    val scroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LargeTitleText(dateRange)
                if (sumIncome != 0.0) {
                    TextValueRow(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        label = "Total Income : ",
                        value = "$${sumIncome.withCommas()}"
                    )
                }
                if (sumExpense != 0.0) {
                    TextValueRow(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        label = "Total Spend : ",
                        value = "$${sumExpense.withCommas()}"
                    )
                }
                HorizontalDivider(thickness = 1.dp)
                if (foodSum != 0.0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                Category.Food.icon,
                                null,
                                tint = Category.Food.color
                            )
                            Text(Category.Food.name)
                        }
                        Text("$${foodSum.withCommas()}")
                    }
                }
                if (transportationSum != 0.0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                Category.Transportation.icon,
                                null,
                                tint = Category.Transportation.color
                            )
                            Text(Category.Transportation.name)
                        }
                        Text("$${transportationSum.withCommas()}")
                    }
                }
                if (entertainmentSum != 0.0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                Category.Entertainment.icon,
                                null,
                                tint = Category.Entertainment.color
                            )
                            Text(Category.Entertainment.name)
                        }
                        Text("$${entertainmentSum.withCommas()}")
                    }
                }
                if (householdSum != 0.0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                Category.Household.icon,
                                null,
                                tint = Category.Household.color
                            )
                            Text(Category.Household.name)
                        }
                        Text("$${householdSum.withCommas()}")
                    }
                }
                if (otherSum != 0.0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        RoundedColumn {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .clickable(onClick = { showOther = !showOther }),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(
                                        Category.Other.icon,
                                        null,
                                        tint = Category.Other.color
                                    )
                                    Text(Category.Other.name)
                                }
                                Text("$${otherSum.withCommas()}")
                            }
                            HorizontalDivider(thickness = 1.dp)
                            if (showOther) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .verticalScroll(scroll),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    otherGrouped.forEach { (customCat, sum) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    vertical = 8.dp,
                                                    horizontal = 16.dp
                                                ),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(customCat)
                                            Text("$${sum.withCommas()}")
                                        }
                                    }
                                }
                                Icon(
                                    Icons.Default.ArrowDropUp,
                                    null,
                                    modifier = Modifier.clickable(onClick = {
                                        showOther = !showOther
                                    })
                                )
                            } else {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    null,
                                    modifier = Modifier.clickable(onClick = {
                                        showOther = !showOther
                                    })
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}