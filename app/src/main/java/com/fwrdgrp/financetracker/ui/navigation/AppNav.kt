package com.fwrdgrp.financetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fwrdgrp.financetracker.ui.screens.home.HomeScreen

@Composable
fun AppNav(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home, modifier = modifier) {
        composable<Screen.Home> {
            HomeScreen(navController)
        }
    }
}