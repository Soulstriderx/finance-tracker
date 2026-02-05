package com.fwrdgrp.financetracker.ui.screens.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.R
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.ui.composables.input.CustomTextField
import com.fwrdgrp.financetracker.ui.navigation.Screen
import com.fwrdgrp.financetracker.ui.theme.OffWhite
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var form by remember { mutableStateOf(LoginReq()) }

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.navigate(Screen.Home) {
                popUpTo<Screen.Login> { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.user.filterNotNull().collect {
            navController.navigate(Screen.Home) {
                popUpTo<Screen.Login> { inclusive = true }
            }
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    Login(
        { viewModel.login(form) },
        { navController.navigate(Screen.Register) },
        form
    ) { form = it }
}

@Composable
fun Login(
    onLogin: () -> Unit,
    navToRegister: () -> Unit,
    form: LoginReq,
    onFormChange: (LoginReq) -> Unit,
) {
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
                Image(
                    painter = painterResource(id = R.drawable.finance_logo),
                    contentDescription = "Description of image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Text(
                    "Login", fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                CustomTextField(
                    "Email",
                    form.email,
                    modifier = Modifier.background(
                        color = OffWhite,
                        shape = RoundedCornerShape(12.dp)
                    )
                )
                { onFormChange(form.copy(email = it)) }
                Spacer(Modifier.height(16.dp))
                CustomTextField(
                    "Password", form.password, isPassword = true,
                    modifier = Modifier.background(
                        color = OffWhite,
                        shape = RoundedCornerShape(12.dp)
                    )
                )
                { onFormChange(form.copy(password = it)) }
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
                            navToRegister()
                        }
                    ) {
                        Text(
                            "Register!",
                            color = Color.Blue,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(Modifier.height(100.dp))
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                onLogin()
            }
        ) {
            Text(
                "Login",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}