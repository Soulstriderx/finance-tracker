package com.fwrdgrp.financetracker.ui.screens.profile

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
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.data.datautils.deriveDateFields
import com.fwrdgrp.financetracker.data.model.main.Bill
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.ProfileUpdateReq
import com.fwrdgrp.financetracker.ui.composables.general.RoundedColumn
import com.fwrdgrp.financetracker.ui.composables.general.TextValueRow
import com.fwrdgrp.financetracker.ui.composables.input.DatePicker
import com.fwrdgrp.financetracker.ui.uiutils.calcOverbudget
import com.fwrdgrp.financetracker.ui.uiutils.calculateAverages
import com.fwrdgrp.financetracker.ui.uiutils.calculateMonthlyBillsTotal
import com.fwrdgrp.financetracker.ui.uiutils.calculateTotalBudget
import com.fwrdgrp.financetracker.ui.uiutils.createProfileForm
import com.fwrdgrp.financetracker.ui.uiutils.getTimeLeft
import com.fwrdgrp.financetracker.ui.uiutils.toCalendar
import com.fwrdgrp.financetracker.ui.uiutils.toRegisterString
import com.fwrdgrp.financetracker.ui.uiutils.toTimestamp
import com.fwrdgrp.financetracker.ui.uiutils.withCommas
import java.util.Calendar

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val transactions by viewModel.transaction.collectAsStateWithLifecycle()
    val bills by viewModel.bills.collectAsStateWithLifecycle()

    user?.let { user ->
        Profile(user, bills, transactions) { viewModel.updateProfile(it) }
    }
}

@Composable
fun Profile(
    user: User,
    bills: List<Bill>,
    transactions: List<Transaction>,
    onUpdate: (ProfileUpdateReq) -> Unit
) {
    var form by remember { mutableStateOf(createProfileForm(user)) }
    val (monthly, weekly, daily) = calculateAverages(transactions)
    var editBalance by remember { mutableStateOf(false) }
    var editBudget by remember { mutableStateOf(false) }
    var editMonthly by remember { mutableStateOf(false) }
    val total = calculateTotalBudget(user.budget)
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RoundedColumn(Modifier.padding(horizontal = 24.dp)) {
                Text(user.name, style = MaterialTheme.typography.displaySmall)
                Text(user.email, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(24.dp))
            HorizontalDivider(thickness = 1.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(Modifier.height(12.dp))
                RoundedColumn(
                    label = "Monthly Overview",
                    canEdit = true,
                    editState = editMonthly,
                    editChange = { editMonthly = it },
                ) {
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Monthly Income",
                        "$${user.monthlyIncome.amount.toDoubleOrNull()?.withCommas() ?: "$0"}",
                        isInput = editMonthly,
                        formValue = form.monthlyIncome.amount
                    ) {
                        val updatedIncome = form.monthlyIncome.copy(amount = it)
                        form = form.copy(monthlyIncome = updatedIncome)
                    }
                    if (editMonthly) {
                        Box(
                            Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                DatePicker(
                                    selectedDate = form.monthlyIncome.payday?.toCalendar()
                                        ?.toRegisterString()
                                        ?: "Select a date",
                                    existingCalendar = user.budget.refresh?.toCalendar()
                                ) {
                                    val derivedDate = deriveDateFields(it.timeInMillis)
                                    val zeroedCalendar = it.apply {
                                        set(Calendar.HOUR_OF_DAY, 0)
                                        set(Calendar.MINUTE, 0)
                                        set(Calendar.SECOND, 0)
                                    }
                                    val updatedIncome = form.monthlyIncome.copy(
                                        payday = zeroedCalendar.toTimestamp(),
                                        day = derivedDate.day
                                    )
                                    form = form.copy(monthlyIncome = updatedIncome)
                                }
                                Spacer(Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        onUpdate(form)
                                        editMonthly = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    } else {
                        TextValueRow(
                            Modifier.padding(horizontal = 12.dp),
                            "Monthly Bills",
                            "$${calculateMonthlyBillsTotal(bills)}"
                        )
                        Text(
                            "Payday in ${getTimeLeft(user.monthlyIncome.payday)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(Modifier.height(0.dp))
                RoundedColumn(
                    label = "Balance",
                    spacer = false,
                    canEdit = true,
                    editState = editBalance,
                    editChange = { editBalance = it },
                ) {
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Current Balance",
                        "$${user.balance.toDouble().withCommas()}",
                        isInput = editBalance,
                        formValue = form.balance ?: ""
                    ) { form = form.copy(balance = it) }
                    if (editBalance) {
                        Box(
                            Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    onUpdate(form)
                                    editBalance = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
                Spacer(Modifier.height(0.dp))
                RoundedColumn(
                    label = "Monthly Budgets",
                    spacer = false,
                    canEdit = true,
                    editState = editBudget,
                    editChange = { editBudget = it }
                ) {
                    Text(
                        "Total: ${total.toDouble().withCommas()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Food",
                        "$${user.budget.foodUsed?.toDouble()?.withCommas()} / ${
                            user.budget.food?.toDouble()?.withCommas()
                        }",
                        isInput = editBudget,
                        formValue = form.budget.food ?: "",
                        isOver = calcOverbudget(
                            user.budget.foodUsed ?: "0",
                            user.budget.food ?: "0"
                        )
                    ) {
                        val updatedBudget = form.budget.copy(food = it)
                        form = form.copy(budget = updatedBudget)
                    }
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Transportation",
                        "$${user.budget.transportationUsed?.toDouble()?.withCommas()} / ${
                            user.budget.transportation?.toDouble()?.withCommas()
                        }",
                        isInput = editBudget,
                        formValue = form.budget.transportation ?: "",
                        isOver = calcOverbudget(
                            user.budget.transportationUsed ?: "0",
                            user.budget.transportation ?: "0"
                        )
                    ) {
                        val updatedBudget = form.budget.copy(transportation = it)
                        form = form.copy(budget = updatedBudget)
                    }
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Entertainment",
                        "$${user.budget.entertainmentUsed?.toDouble()?.withCommas()} / ${
                            user.budget.entertainment?.toDouble()?.withCommas()
                        }",
                        isInput = editBudget,
                        formValue = form.budget.entertainment ?: "",
                        isOver = calcOverbudget(
                            user.budget.entertainmentUsed ?: "0",
                            user.budget.entertainment ?: "0"
                        )
                    ) {
                        val updatedBudget = form.budget.copy(entertainment = it)
                        form = form.copy(budget = updatedBudget)
                    }
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Household",
                        "$${user.budget.householdUsed?.toDouble()?.withCommas()} / ${
                            user.budget.household?.toDouble()?.withCommas()
                        }",
                        isInput = editBudget,
                        formValue = form.budget.household ?: "",
                        isOver = calcOverbudget(
                            user.budget.householdUsed ?: "0",
                            user.budget.household ?: "0"
                        )
                    ) {
                        val updatedBudget = form.budget.copy(household = it)
                        form = form.copy(budget = updatedBudget)
                    }
                    if (editBudget) {
                        Box(
                            Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                DatePicker(
                                    selectedDate = form.budget.refresh?.toCalendar()
                                        ?.toRegisterString()
                                        ?: "Select a date",
                                    existingCalendar = user.budget.refresh?.toCalendar()
                                ) {
                                    val derivedDate = deriveDateFields(it.timeInMillis)
                                    val zeroedCalendar = it.apply {
                                        set(Calendar.HOUR_OF_DAY, 0)
                                        set(Calendar.MINUTE, 0)
                                        set(Calendar.SECOND, 0)
                                    }
                                    val updatedBudget = form.budget.copy(
                                        refresh = zeroedCalendar.toTimestamp(),
                                        day = derivedDate.day
                                    )
                                    form = form.copy(budget = updatedBudget)
                                }
                                Spacer(Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        onUpdate(form)
                                        editBudget = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    } else {
                        Text(
                            "Refresh in ${getTimeLeft(user.budget.refresh)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(Modifier.height(0.dp))
                RoundedColumn(label = "Averages") {
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Monthly",
                        "$%.2f".format(monthly)
                    )
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Weekly",
                        "$%.2f".format(weekly)
                    )
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Daily",
                        "$%.2f".format(daily)
                    )
                }
                Spacer(Modifier.height(0.dp))

                RoundedColumn(label = "Lifetimes") {
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Lifetime Spend",
                        "$${user.lifetimeSpend.toDouble().withCommas()}"
                    )
                    TextValueRow(
                        Modifier.padding(horizontal = 12.dp),
                        "Lifetime Income",
                        "$${user.lifetimeIncome.toDouble().withCommas()}"
                    )
                }
                Spacer(Modifier.height(100.dp))
            }
        }
    }
}