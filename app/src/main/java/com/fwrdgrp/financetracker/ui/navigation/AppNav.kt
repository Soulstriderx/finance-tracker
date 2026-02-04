package com.fwrdgrp.financetracker.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fwrdgrp.financetracker.ui.composables.scaffold.AddExpenseFab
import com.fwrdgrp.financetracker.ui.composables.scaffold.BottomNavBar
import com.fwrdgrp.financetracker.ui.composables.scaffold.CustomTopBar
import com.fwrdgrp.financetracker.ui.navigation.auth.AuthViewModel
import com.fwrdgrp.financetracker.ui.screens.auth.login.LoginScreen
import com.fwrdgrp.financetracker.ui.screens.auth.register.RegisterScreen
import com.fwrdgrp.financetracker.ui.screens.bills.BillsScreen
import com.fwrdgrp.financetracker.ui.screens.bills.details.BillDetailScreen
import com.fwrdgrp.financetracker.ui.screens.breakdown.BreakdownScreen
import com.fwrdgrp.financetracker.ui.screens.home.HomeScreen
import com.fwrdgrp.financetracker.ui.screens.manage.add.AddTransactionScreen
import com.fwrdgrp.financetracker.ui.screens.manage.edit.EditTransactionScreen
import com.fwrdgrp.financetracker.ui.screens.profile.ProfileScreen
import com.fwrdgrp.financetracker.ui.screens.stats.StatsScreen
import com.fwrdgrp.financetracker.ui.screens.transaction.TransactionScreen
import com.fwrdgrp.financetracker.ui.screens.transactiondetails.TransactionDetailsScreen
import com.fwrdgrp.financetracker.ui.theme.CreamyTan
import com.fwrdgrp.financetracker.ui.theme.Jeremy

@Composable
fun AppNav(
    modifier: Modifier = Modifier,
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val curDest = navBackStackEntry?.destination

    val showBottomBar = when {
        curDest == null -> false
        curDest.hasRoute<Screen.Add>() ||
                curDest.hasRoute<Screen.Edit>() ||
                curDest.hasRoute<Screen.TranDetails>() ||
                curDest.hasRoute<Screen.Login>() ||
                curDest.hasRoute<Screen.Breakdown>() ||
                curDest.hasRoute<Screen.BillDetails>() ||
                curDest.hasRoute<Screen.Register>() -> false

        else -> true
    }

    val showTopBar = when {
        curDest == null -> false
        curDest.hasRoute<Screen.Login>() ||
                curDest.hasRoute<Screen.Register>() -> false

        else -> true
    }

    val showLogout = when {
        curDest == null -> false
        curDest.hasRoute<Screen.Profile>() -> true
        else -> false
    }

    val showBackBtn = when {
        curDest == null -> false
        curDest.hasRoute<Screen.Login>() ||
                curDest.hasRoute<Screen.Register>() ||
                curDest.hasRoute<Screen.Home>() ||
                curDest.hasRoute<Screen.Transaction>() ||
                curDest.hasRoute<Screen.Profile>() ||
                curDest.hasRoute<Screen.Bills>() ||
                curDest.hasRoute<Screen.Stats>() -> false

        else -> true
    }

    val label = when {
        curDest == null -> ""
        curDest.hasRoute<Screen.Home>() -> "Home"
        curDest.hasRoute<Screen.Add>() -> "Add Transaction"
        curDest.hasRoute<Screen.Edit>() -> "Edit Transaction"
        curDest.hasRoute<Screen.Transaction>() -> "Transactions"
        curDest.hasRoute<Screen.Stats>() -> "Stats"
        curDest.hasRoute<Screen.TranDetails>() -> "Transaction Details"
        curDest.hasRoute<Screen.Profile>() -> "Profile"
        curDest.hasRoute<Screen.Breakdown>() -> "Transaction Breakdown"
        curDest.hasRoute<Screen.Bills>() -> "Bills"
        curDest.hasRoute<Screen.BillDetails>() -> "Bill Details"
        else -> ""
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(curDest)
                { screen ->
                    navController.navigate(screen) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                Box(modifier = Modifier.offset(y = 58.dp)) {
                    AddExpenseFab(onClick = { navController.navigate(Screen.Add) })
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (showTopBar) {
                    CustomTopBar(
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            )
                            .background(
                                color = Jeremy,
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            )
                            .padding(top = innerPadding.calculateTopPadding()),
                        navController = navController,
                        label = label,
                        showBackButton = showBackBtn,
                        showLogout = showLogout,
                        authService = viewModel.authService
                    )
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
        composable<Screen.Add> { AddTransactionScreen(navController) }
        composable<Screen.Edit> { EditTransactionScreen(navController) }
        composable<Screen.Transaction> { TransactionScreen(navController) }
        composable<Screen.TranDetails> { TransactionDetailsScreen(navController) }
        composable<Screen.Stats> { StatsScreen(navController) }
        composable<Screen.Profile> { ProfileScreen() }
        composable<Screen.Breakdown> { BreakdownScreen() }
        composable<Screen.Bills> { BillsScreen(navController) }
        composable<Screen.BillDetails> { BillDetailScreen(navController) }
    }
}