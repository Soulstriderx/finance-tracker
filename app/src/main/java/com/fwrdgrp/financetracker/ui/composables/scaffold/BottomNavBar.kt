package com.fwrdgrp.financetracker.ui.composables.scaffold

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.fwrdgrp.financetracker.ui.navigation.Screen

@Composable
fun BottomNavBar(
    currentRoute: NavDestination?,
    onNavigate: (Screen) -> Unit
) {
    var page by remember { mutableStateOf(0) }
    NavigationBar(
        containerColor = Color(0xFFF5EFE6)
    ) {
        if (page == 0) {
            NavigationBarItem(
                selected = currentRoute?.hasRoute<Screen.Home>() == true,
                onClick = {
                    if (currentRoute?.hasRoute<Screen.Home>() != true)
                        onNavigate(Screen.Home)
                },
                icon = { Icon(Icons.Outlined.Home, null) },
                label = { Text("Home") }
            )

            NavigationBarItem(
                selected = currentRoute?.hasRoute<Screen.Transaction>() == true,
                onClick = {
                    if (currentRoute?.hasRoute<Screen.Transaction>() != true)
                        onNavigate(Screen.Transaction)
                },
                icon = { Icon(Icons.AutoMirrored.Outlined.List, null) },
                label = { Text("Transactions") }
            )
        } else {
            NavigationBarItem(
                selected = page == 99,
                onClick = { page = 0 },
                icon = { Icon(Icons.Default.ChevronLeft, null) },
                label = { Text("Go Back") }
            )

            NavigationBarItem(
                selected = currentRoute?.hasRoute<Screen.Bills>() == true,
                onClick = {
                    if (currentRoute?.hasRoute<Screen.Bills>() != true)
                        onNavigate(Screen.Bills)
                },
                icon = {
                    Icon(
                        Icons.AutoMirrored.Outlined.InsertDriveFile,
                        null
                    )
                },
                label = { Text("Bills") }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (page == 0) {
            NavigationBarItem(
                selected = currentRoute?.hasRoute<Screen.Profile>() == true,
                onClick = {
                    if (currentRoute?.hasRoute<Screen.Profile>() != true)
                        onNavigate(Screen.Profile)
                },
                icon = { Icon(Icons.Outlined.Person, null) },
                label = { Text("Profile") }
            )
            NavigationBarItem(
                selected = page == 99,
                onClick = { page = 1 },
                icon = { Icon(Icons.Default.ChevronRight, null) },
                label = { Text("Next Page") }
            )
        } else {
            NavigationBarItem(
                selected = currentRoute?.hasRoute<Screen.Stats>() == true,
                onClick = {
                    if (currentRoute?.hasRoute<Screen.Stats>() != true)
                        onNavigate(Screen.Stats)
                },
                icon = { Icon(Icons.Outlined.PieChart, null) },
                label = { Text("Stats") }
            )
            NavigationBarItem(
                selected = page == 99,
                onClick = {  },
                icon = { Icon(Icons.Default.QuestionMark, null) },
                label = { Text("Soon") }
            )
        }
    }
}
