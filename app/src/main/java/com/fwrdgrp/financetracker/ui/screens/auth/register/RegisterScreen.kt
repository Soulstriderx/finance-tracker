package com.fwrdgrp.financetracker.ui.screens.auth.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.data.model.main.MonthlyIncome
import com.fwrdgrp.financetracker.data.model.request.RegisterReq
import com.fwrdgrp.financetracker.ui.composables.input.CustomTextField
import com.fwrdgrp.financetracker.ui.composables.input.DatePicker
import com.fwrdgrp.financetracker.ui.theme.OffWhite
import com.fwrdgrp.financetracker.ui.uiutils.toCalendar
import com.fwrdgrp.financetracker.ui.uiutils.toRegisterString
import com.google.firebase.Timestamp
import java.util.Calendar

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    var form by remember { mutableStateOf(RegisterReq()) }
    var showDialog by remember { mutableStateOf(false) }

    var checked by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.popBackStack()
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            content = { Text("Select Date") },
        )
    }

    Register(
        { viewModel.register(form, it, checked) },
        { navController.popBackStack() },
        checked,
        { checked = !checked },
        form
    ) { form = it }
}

@Composable
fun Register(
    onRegister: (MonthlyIncome) -> Unit,
    navToLogin: () -> Unit,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    form: RegisterReq,
    onFormChange: (RegisterReq) -> Unit,
) {
    var income by remember { mutableStateOf(MonthlyIncome()) }

    val paydayCalendar = income.payday?.toCalendar()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Register", fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                CustomTextField("Name", form.name,
                    modifier = Modifier.background(color = OffWhite, shape = RoundedCornerShape(12.dp)))
                { onFormChange(form.copy(name = it)) }
                Spacer(Modifier.height(16.dp))
                CustomTextField("Email", form.email,
                    modifier = Modifier.background(color = OffWhite, shape = RoundedCornerShape(12.dp)))
                { onFormChange(form.copy(email = it)) }
                Spacer(Modifier.height(16.dp))
                CustomTextField("Password", form.password, isPassword = true,
                    modifier = Modifier.background(color = OffWhite, shape = RoundedCornerShape(12.dp)))
                { onFormChange(form.copy(password = it)) }
                Spacer(Modifier.height(16.dp))
                CustomTextField("Confirm Password", form.password2, isPassword = true,
                    modifier = Modifier.background(color = OffWhite, shape = RoundedCornerShape(12.dp)))
                { onFormChange(form.copy(password2 = it)) }
                Spacer(Modifier.height(16.dp))
                CustomTextField("Balance", form.balance ?: "",
                    modifier = Modifier.background(color = OffWhite, shape = RoundedCornerShape(12.dp)))
                { onFormChange(form.copy(balance = it)) }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { onChecked(!checked) }
                    )
                    Text("Do you have monthly income?")
                }
                Spacer(Modifier.height(10.dp))
                if (checked) {
                    CustomTextField("Monthly Income", income.amount,
                        modifier = Modifier.background(color = OffWhite, shape = RoundedCornerShape(12.dp)))
                    { income = (income.copy(amount = it)) }
                    Spacer(Modifier.height(16.dp))
                    DatePicker(
                        selectedDate = paydayCalendar?.toRegisterString() ?: "",
                        existingCalendar = paydayCalendar,
                        onDateSelected = { calendar ->
                            val timestamp = Timestamp(calendar.time)
                            income = (
                                income.copy(
                                    day = calendar.get(Calendar.DAY_OF_MONTH),
                                    payday = timestamp
                                )
                            )
                        }
                    )
                }
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Don't have an account?",
                        fontSize = 16.sp
                    )
                    TextButton(
                        onClick = {
                            navToLogin()
                        }
                    ) {
                        Text(
                            "Sign-in!",
                            color = Color.Blue,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                onRegister(income)
            }
        ) {
            Text(
                "Register",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}