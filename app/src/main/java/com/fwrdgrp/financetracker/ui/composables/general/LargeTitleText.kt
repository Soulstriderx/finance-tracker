package com.fwrdgrp.financetracker.ui.composables.general

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun LargeTitleText(label: String) {
    Text(
        label,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Medium
    )
}