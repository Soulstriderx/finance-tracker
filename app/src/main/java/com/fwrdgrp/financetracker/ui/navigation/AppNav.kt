package com.fwrdgrp.financetracker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fwrdgrp.financetracker.ui.composables.scaffold.AddExpenseFab
import com.fwrdgrp.financetracker.ui.composables.scaffold.BottomNavBar
import com.fwrdgrp.financetracker.ui.composables.scaffold.CustomTopBar
import com.fwrdgrp.financetracker.ui.screens.auth.login.LoginScreen
import com.fwrdgrp.financetracker.ui.screens.auth.register.RegisterScreen
import com.fwrdgrp.financetracker.ui.screens.home.HomeScreen

@Composable
fun AppNav(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val curDest = navBackStackEntry?.destination

    val showBottomBar = when {
        curDest == null -> false
        curDest.hasRoute<Screen.Login>() ||
        curDest.hasRoute<Screen.Register>() -> false
        else -> true
    }

    val showTopBar = when {
        curDest == null -> false
        curDest.hasRoute<Screen.Login>() ||
        curDest.hasRoute<Screen.Register>() -> false
        else -> true
    }

    val showBackBtn = when {
        curDest == null -> false
        curDest.hasRoute<Screen.Login>() ||
        curDest.hasRoute<Screen.Register>() ||
        curDest.hasRoute<Screen.Home>() -> false
        else -> true
    }

    val label = when {
        curDest == null -> ""
        curDest.hasRoute<Screen.Home>() -> "Home"
//        curDest.hasRoute<Screen.Add>() -> "Add Expense"
//        curDest.hasRoute<Screen.Update>() -> "Update Expense"
//        curDest.hasRoute<Screen.Profile>() -> "Profile"
        else -> ""
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(curDest)
                { screen -> navController.navigate(screen) }
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                Box(modifier = Modifier.offset(y = 58.dp)) {
                    AddExpenseFab(
                        onClick = { /*navController.navigate(Screen.Add)*/ }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (showTopBar) {
                    HorizontalDivider(thickness = 1.dp)
                    CustomTopBar(
                        navController = navController,
                        label = label,
                        showBackButton = showBackBtn
                    )
                    HorizontalDivider(thickness = 1.dp)
                }
                Nav(modifier, navController)
            }
        }
    }
}

@Composable
fun Nav(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login, modifier = modifier) {
        composable<Screen.Login> { LoginScreen(navController) }
        composable<Screen.Register> { RegisterScreen(navController) }
        composable<Screen.Home> { HomeScreen(navController) }
    }
}