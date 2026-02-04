package com.fwrdgrp.financetracker.ui.composables.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.ui.theme.CreamyTan

@Composable
fun RoundedColumn(
    modifier: Modifier = Modifier,
    label: String? = null,
    spacer: Boolean = true,
    canEdit: Boolean = false,
    editState: Boolean = false,
    editChange: ((Boolean) -> Unit)? = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(8.dp))
            .background(
                color = CreamyTan,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (label != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.size(20.dp))
                LargeTitleText(label)
                if (canEdit) {
                    IconButton(
                        onClick = { editChange?.invoke(!editState) }
                    ) {
                        Icon(
                            if (editState) Icons.Filled.EditOff else Icons.Filled.Edit,
                            null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Spacer(Modifier.size(20.dp))
                }
            }
            if (spacer) Spacer(Modifier.height(0.dp))
        }
        content()
    }
}