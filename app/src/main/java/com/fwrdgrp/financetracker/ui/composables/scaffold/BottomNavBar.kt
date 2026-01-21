package com.fwrdgrp.financetracker.ui.composables.scaffold

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    NavigationBar(
        containerColor = Color(0xFFF5EFE6)
    ) {
        NavigationBarItem(
            selected = currentRoute?.hasRoute<Screen.Home>() == true,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(Icons.Outlined.Home, null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = currentRoute?.hasRoute<Screen.Home>() != true,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(Icons.Outlined.List, null) },
            label = { Text("Transactions") }
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationBarItem(
            selected = currentRoute?.hasRoute<Screen.Home>() == true,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(Icons.Outlined.PieChart, null) },
            label = { Text("Stats") }
        )

        NavigationBarItem(
            selected = currentRoute?.hasRoute<Screen.Home>() == true,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(Icons.Outlined.Person, null) },
            label = { Text("Profile") }
        )
    }
}
