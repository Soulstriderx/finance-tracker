package com.fwrdgrp.financetracker.data.enum

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Interests
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


enum class Category(val color: Color, val icon: ImageVector) {
    Food(Color(0xFFFF6B6B), Icons.Filled.Fastfood),
    Transportation(Color(0xFF4ECDC4), Icons.Filled.LocalTaxi),
    Entertainment(Color(0xFFFFBE0B), Icons.Filled.SportsEsports),
    Household(Color(0xFF95E1D3), Icons.Filled.House),
    Other(Color(0xFFB4A7D6), Icons.Filled.Interests)
}