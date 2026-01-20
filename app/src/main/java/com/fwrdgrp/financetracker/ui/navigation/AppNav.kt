package com.fwrdgrp.financetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fwrdgrp.financetracker.ui.screens.auth.login.LoginScreen
import com.fwrdgrp.financetracker.ui.screens.auth.register.RegisterScreen
import com.fwrdgrp.financetracker.ui.screens.home.HomeScreen

@Composable
fun AppNav(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login, modifier = modifier) {
        composable<Screen.Login> { LoginScreen(navController) }
        composable<Screen.Register> { RegisterScreen(navController) }
        composable<Screen.Home> { HomeScreen(navController) }
    }
}