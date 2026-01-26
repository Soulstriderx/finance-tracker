package com.fwrdgrp.financetracker.ui.screens.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.ui.composables.general.SelectionButtons
import com.fwrdgrp.financetracker.ui.composables.home.ExpenseRow
import com.fwrdgrp.financetracker.ui.composables.input.DatePicker
import com.fwrdgrp.financetracker.ui.uiutils.toFullTextDate
import com.fwrdgrp.financetracker.ui.uiutils.toMonthRangeText
import com.fwrdgrp.financetracker.ui.uiutils.toWeekRangeText
import java.util.Calendar

@Composable
fun TransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    var calendar by remember { mutableStateOf(Calendar.getInstance()) }

    val selectedTab by viewModel.dateFilter.collectAsStateWithLifecycle()
    val tabs = DateFilter.entries

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(Modifier.height(0.dp))
                SelectionButtons(
                    tabs,
                    selectedTab,
                    { it.name }
                ) { viewModel.onDateFilterSelect(it, calendar) }
                DatePicker(
                    selectedDate = calendar.toFullTextDate(),
                    existingCalendar = calendar,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onDateSelected = {
                        calendar = it
                        viewModel.onDateFilterSelect(selectedTab, it)
                    }
                )
                Spacer(Modifier.height(0.dp))
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            Spacer(Modifier.height(6.dp))
            Text(
                text = when (selectedTab) {
                    DateFilter.Daily -> calendar.toFullTextDate()
                    DateFilter.Weekly -> calendar.toWeekRangeText()
                    DateFilter.Monthly -> calendar.toMonthRangeText()
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                items(transactions) { item ->
                    ExpenseRow(item)
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}