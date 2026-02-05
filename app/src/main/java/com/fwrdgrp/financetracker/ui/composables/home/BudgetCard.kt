package com.fwrdgrp.financetracker.ui.composables.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.data.model.main.Budget
import com.fwrdgrp.financetracker.ui.composables.general.LargeTitleText
import com.fwrdgrp.financetracker.ui.composables.general.RoundedColumn
import com.fwrdgrp.financetracker.ui.composables.general.TextValueRow
import com.fwrdgrp.financetracker.ui.uiutils.getLowBudgetWarning
import com.fwrdgrp.financetracker.ui.uiutils.withCommas

@Composable
fun BudgetCard(budget: Budget, showBudget: Boolean, onClick: (Boolean) -> Unit) {
    val warning = getLowBudgetWarning(budget)
    RoundedColumn(modifier = Modifier.clickable(onClick = { onClick(!showBudget) })) {
        if (showBudget) {
            LargeTitleText("Budget")
            if (budget.food?.toIntOrNull() != null && budget.food.toInt() != 0) {
                TextValueRow(
                    Modifier.padding(horizontal = 12.dp),
                    "Food",
                    "$${
                        budget.foodUsed?.toDouble()?.withCommas()
                    } / ${budget.food.toDouble().withCommas()}"
                )
            }
            if (budget.transportation?.toIntOrNull() != null && budget.transportation.toInt() != 0) {
                TextValueRow(
                    Modifier.padding(horizontal = 12.dp),
                    "Transportation",
                    "$${
                        budget.transportationUsed?.toDouble()?.withCommas()
                    } / ${budget.transportation.toDouble().withCommas()}"
                )
            }
            if (budget.entertainment?.toIntOrNull() != null && budget.entertainment.toInt() != 0) {
                TextValueRow(
                    Modifier.padding(horizontal = 12.dp),
                    "Entertainment",
                    "$${
                        budget.entertainmentUsed?.toDouble()?.withCommas()
                    } / ${budget.entertainment.toDouble().withCommas()}"
                )
            }
            if (budget.household?.toIntOrNull() != null && budget.household.toInt() != 0) {
                TextValueRow(
                    Modifier.padding(horizontal = 12.dp),
                    "Household",
                    "$${
                        budget.householdUsed?.toDouble()?.withCommas()
                    } / ${budget.household.toDouble().withCommas()}"
                )
            }
            warning?.let {
                Text(
                    warning,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp).fillMaxWidth()
                )
            }
            Icon(Icons.Outlined.ArrowDropUp, null)
        } else {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(24.dp))
                LargeTitleText("Budget")
                Icon(Icons.Outlined.ArrowDropDown, null)
            }
        }
    }
}