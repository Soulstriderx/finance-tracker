package com.fwrdgrp.financetracker.ui.composables.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.ui.PieChartData
import com.fwrdgrp.financetracker.ui.composables.charts.PieChart
import com.fwrdgrp.financetracker.ui.composables.general.TabButtons

@Composable
fun BreakdownCard(
    tabs: List<DateFilter>,
    pieData: List<PieChartData>,
    selectedTab: DateFilter,
    onTabSelect: (DateFilter) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(8.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 12.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Spending Breakdown - Month",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(thickness = 1.dp)
            TabButtons(tabs, selectedTab, { it.name })
            { onTabSelect(it) }
            HorizontalDivider(thickness = 1.dp)
            if (pieData.isNotEmpty()) {
                PieChart(
                    data = pieData,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            } else {
                Spacer(Modifier.height(24.dp))
                Text("Add a transaction", textAlign = TextAlign.Center)
                Text("to see the breakdown", textAlign = TextAlign.Center)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}