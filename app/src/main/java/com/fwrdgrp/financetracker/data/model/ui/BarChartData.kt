package com.fwrdgrp.financetracker.data.model.ui

import com.fwrdgrp.financetracker.data.enum.BarData

data class BarChartData(
    val data: List<BarData>,
    val maxValue: Float,
    val average: Float
)