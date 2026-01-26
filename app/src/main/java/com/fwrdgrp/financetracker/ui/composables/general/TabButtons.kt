package com.fwrdgrp.financetracker.ui.composables.general

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> TabButtons(
    tabs: List<T>,
    selectedTab: T,
    label: (T) -> String = { "" },
    isIconButton: Boolean = false,
    iconLabel: (T) -> ImageVector = { Icons.Filled.Category },
    colorLabel: (T) -> Color = { Color.White },
    onTabSelect: (T) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { index, tab ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(
                        color = if (selectedTab == tab) Color.Black
                        else Color.White
                    )
                    .clickable { onTabSelect(tab) },
                contentAlignment = Alignment.Center
            ) {
                if (isIconButton) {
                    Icon(
                        iconLabel(tab),
                        "",
                        Modifier.size(30.dp),
                        tint = colorLabel(tab)
                    )
                } else {
                    Text(
                        text = label(tab),
                        fontSize = 14.sp,
                        color = if (selectedTab == tab) Color.White
                        else Color.Black
                    )
                }
            }
            if (index < tabs.size - 1) {
                VerticalDivider(thickness = 1.dp, modifier = Modifier.height(48.dp))
            }
        }
    }
}