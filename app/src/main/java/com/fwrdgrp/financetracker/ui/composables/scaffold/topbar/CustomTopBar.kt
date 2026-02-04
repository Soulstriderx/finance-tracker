package com.fwrdgrp.financetracker.ui.composables.scaffold.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fwrdgrp.financetracker.ui.navigation.Screen

@Composable
fun CustomTopBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    label: String = "",
    showBackButton: Boolean,
    showLogout: Boolean,
    viewModel: TopBarViewModel = hiltViewModel()
) {
    val size = 28.dp
    Box(
        contentAlignment = Alignment.Center, modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 10.dp, horizontal = 16.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "Back button",
                    modifier = Modifier
                        .size(size)
                        .clickable { navController.popBackStack() })
                Text(
                    label, style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.width(size))
            } else {
                Spacer(Modifier.width(size))
                Text(
                    label, style = MaterialTheme.typography.titleLarge
                )
                if (showLogout) {
                    IconButton(
                        onClick = {
                            viewModel.signOut()
                            navController.navigate(Screen.Login) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, null)
                    }
                } else {
                    Spacer(Modifier.width(size))
                }
            }
        }
    }
}