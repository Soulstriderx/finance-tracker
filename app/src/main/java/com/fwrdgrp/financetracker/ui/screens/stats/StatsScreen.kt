package com.fwrdgrp.financetracker.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.fwrdgrp.financetracker.ui.composables.charts.BarChart
import com.fwrdgrp.financetracker.ui.composables.general.SelectionButtons
import com.fwrdgrp.financetracker.ui.composables.input.DatePicker
import com.fwrdgrp.financetracker.ui.uiutils.calculateChartData
import com.fwrdgrp.financetracker.ui.uiutils.generateTrendDescription
import com.fwrdgrp.financetracker.ui.uiutils.toFullTextDate
import java.util.Calendar

@Composable
fun StatsScreen(
    navController: NavController,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    var calendar by remember { mutableStateOf(Calendar.getInstance()) }

    val scrollState = rememberScrollState()

    val selectedTab by viewModel.dateFilter.collectAsStateWithLifecycle()
    val tabs = DateFilter.entries

    val chartData = remember(selectedTab, transactions, calendar) {
        calculateChartData(selectedTab, transactions, calendar)
    }
    val trendDescription = remember(chartData, selectedTab) {
        generateTrendDescription(chartData.data, selectedTab)
    }

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(Modifier.height(0.dp))
                BarChart(
                    data = chartData.data,
                    maxValue = chartData.maxValue,
                    average = chartData.average,
                )
                Text(
                    text = trendDescription,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(Modifier.height(120.dp))
            }
        }
    }
}