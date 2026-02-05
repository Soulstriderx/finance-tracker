package com.fwrdgrp.financetracker.ui.composables.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fwrdgrp.financetracker.ui.theme.AlmostBlack
import com.fwrdgrp.financetracker.ui.theme.CreamyTan

@Composable
fun <T> SelectionButtons(
    tabs: List<T>,
    selectedTab: T,
    label: (T) -> String = { "" },
    onTabSelect: (T) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        tabs.forEach { tab ->
            Button(
                onClick = { onTabSelect(tab) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == tab) AlmostBlack else CreamyTan,
                    contentColor = if (selectedTab == tab) CreamyTan else AlmostBlack
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.7f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = label(tab),
                    fontSize = 14.sp,
                )
            }
        }
    }
}