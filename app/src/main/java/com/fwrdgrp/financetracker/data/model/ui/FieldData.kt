package com.fwrdgrp.financetracker.data.model.ui

data class FieldData(
    val label: String,
    val value: String,
    val list: List<String> = emptyList(),
    val isPassword: Boolean = false,
    val readOnly: Boolean = false,
    val onValueChange: (String) -> Unit,
)