package com.fwrdgrp.financetracker.ui.composables.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.fwrdgrp.financetracker.data.enum.BarData

@Composable
fun BarChart(
    data: List<BarData>,
    maxValue: Float,
    average: Float,
) {
    Canvas(modifier = Modifier.fillMaxSize().height(400.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val leftPadding = 80f
        val rightPadding = 120f
        val topPadding = 40f
        val bottomPadding = 60f

        val chartWidth = canvasWidth - leftPadding - rightPadding
        val chartHeight = canvasHeight - topPadding - bottomPadding

        val barWidth = chartWidth / (data.size * 2)
        val barSpacing = barWidth

        // Draw Y-axis labels
        val ySteps = 5
        for (i in 0..ySteps) {
            val y = topPadding + chartHeight - (chartHeight * i / ySteps)
            val value = maxValue * i / ySteps

            drawContext.canvas.nativeCanvas.drawText(
                "%.0f".format(value),
                20f,
                y + 5f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 28f
                }
            )
        }

        // Draw bars
        data.forEachIndexed { index, item ->
            val barHeight = (item.spend / maxValue) * chartHeight
            val x = leftPadding + index * (barWidth + barSpacing) + barSpacing / 2
            val y = topPadding + chartHeight - barHeight

            // Bar
            drawRect(
                color = Color(0xFF3B82F6),
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )

            // X-axis label
            drawContext.canvas.nativeCanvas.drawText(
                item.name,
                x + barWidth / 2,
                topPadding + chartHeight + 40f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 28f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }

        // Draw average line (dashed)
        val avgY = topPadding + chartHeight - (average / maxValue * chartHeight)
        drawLine(
            color = Color(0xFFFF6B6B),
            start = Offset(leftPadding, avgY),
            end = Offset(leftPadding + chartWidth, avgY),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
        )

        drawLine(
            color = Color.Black,
            start = Offset(leftPadding, topPadding + chartHeight),
            end = Offset(leftPadding + chartWidth, topPadding + chartHeight),
            strokeWidth = 2f
        )

        // Average label
        drawContext.canvas.nativeCanvas.drawText(
            "Avg: ${"%.2f".format(average)}",
            leftPadding + chartWidth + 10f,
            avgY + 5f,
            android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#FF6B6B")
                textSize = 28f
            }
        )
    }
}